package springboot.shoppingmall.authorization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.authorization.domain.RefreshToken;
import springboot.shoppingmall.authorization.domain.RefreshTokenRepository;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.authorization.exception.ExpireTokenException;
import springboot.shoppingmall.authorization.exception.TryLoginLockedUserException;
import springboot.shoppingmall.authorization.exception.WrongPasswordException;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserFinder userFinder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse login(String loginId, String password, String accessIp) throws WrongPasswordException {
        User loginUser = userFinder.findUserByLoginId(loginId);
        if(loginUser.isLocked()) {
            throw new TryLoginLockedUserException("해당 계정은 로그인 실패 횟수 5회를 초과하여 로그인 할 수 없습니다. 관리자에게 문의해주세요.");
        }

        if(!loginUser.isEqualPassword(password)) {
            int loginFailCount = loginUser.increaseLoginFailCount();
            throw new WrongPasswordException("비밀번호가 틀렸습니다. 5회 이상 실패하시는 경우, 로그인하실 수 없습니다. (현재 " + loginFailCount + " / 5)");
        }

        String accessToken = jwtTokenProvider.createAccessToken(loginUser, accessIp);
        String refreshToken = jwtTokenProvider.createRefreshToken(loginUser, accessIp);

        saveRefreshToken(loginUser, refreshToken);

        return new TokenResponse(accessToken, refreshToken);
    }

    private void saveRefreshToken(User loginUser, String refreshToken) {
        boolean isPresent = refreshTokenRepository.findByUser(loginUser).isPresent();
        if(isPresent){
            refreshTokenRepository.deleteByUser(loginUser);
        }
        refreshTokenRepository.save(new RefreshToken(loginUser, refreshToken));
    }

    @Transactional
    public TokenResponse logout(Long id){
        User user = userFinder.findUserById(id);
        refreshTokenRepository.deleteByUser(user);
        String expireToken = jwtTokenProvider.createExpireToken();
        return new TokenResponse(expireToken);
    }

    public AuthorizedUser getAuthorizedUser(String token, String ip){
        if(!jwtTokenProvider.validateExpireToken(token)){
            throw new ExpireTokenException("인증 기한이 만료되었습니다.");
        }

        if(!jwtTokenProvider.validateIpToken(token, ip)) {
            throw new IllegalStateException("최초에 로그인한 IP와 다른 IP 에서 접속하셨습니다. 보안을 위해 로그아웃 됩니다.");
        }

        Long userId = jwtTokenProvider.getUserId(token);
        User user = userFinder.findUserById(userId);
        return new AuthorizedUser(user.getId(), user.getLoginId());
    }

    public TokenResponse reCreateAccessToken(String token, String accessIp){
        Long userId = jwtTokenProvider.getUserId(token);
        User user = userFinder.findUserById(userId);
        String refreshToken = findRefreshTokenByUser(user).getRefreshToken();
        if(!jwtTokenProvider.validateExpireToken(refreshToken)){
            throw new ExpireTokenException("인증 만료됨");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user, accessIp);
        return new TokenResponse(accessToken, refreshToken);
    }

    private RefreshToken findRefreshTokenByUser(User user) {
        return refreshTokenRepository.findByUser(user)
                .orElseThrow(
                        () -> new ExpireTokenException("인증 만료됨")
                );
    }
}
