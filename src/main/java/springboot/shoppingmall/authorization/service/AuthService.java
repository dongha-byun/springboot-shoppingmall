package springboot.shoppingmall.authorization.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.authorization.domain.RefreshToken;
import springboot.shoppingmall.authorization.domain.RefreshTokenRepository;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.authorization.exception.ExpireTokenException;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.authorization.dto.LoginRequest;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse login(LoginRequest loginRequest, String accessIp) {
        User loginUser = getLoginUser(loginRequest);
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
        User user = findUserById(id);
        refreshTokenRepository.deleteByUser(user);
        String expireToken = jwtTokenProvider.createExpireToken();
        return new TokenResponse(expireToken);
    }

    public AuthorizedUser getAuthorizedUser(String token){
        if(!jwtTokenProvider.validateExpireToken(token)){
            throw new ExpireTokenException("?????? ????????? ?????????????????????.");
        }

        Long userId = jwtTokenProvider.getUserId(token);
        User user = findUserById(userId);
        return new AuthorizedUser(user.getId(), user.getLoginId());
    }

    private User getLoginUser(LoginRequest loginRequest) {
        return userRepository.findUserByLoginId(loginRequest.getLoginId())
                .filter(user -> user.isEqualPassword(loginRequest.getPassword()))
                .orElseThrow(
                        () -> new IllegalArgumentException("?????? ?????? ??????")
                );
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public TokenResponse reCreateAccessToken(String token, String accessIp){
        Long userId = jwtTokenProvider.getUserId(token);
        User user = findUserById(userId);
        String refreshToken = findRefreshTokenByUser(user).getRefreshToken();
        if(!jwtTokenProvider.validateExpireToken(refreshToken)){
            throw new ExpireTokenException("?????? ?????????");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user, accessIp);
        return new TokenResponse(accessToken, refreshToken);
    }

    private RefreshToken findRefreshTokenByUser(User user) {
        return refreshTokenRepository.findByUser(user)
                .orElseThrow(
                        () -> new ExpireTokenException("?????? ?????????")
                );
    }
}
