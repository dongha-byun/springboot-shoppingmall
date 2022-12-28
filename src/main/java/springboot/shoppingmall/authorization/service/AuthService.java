package springboot.shoppingmall.authorization.service;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.authorization.JwtTokenProvider;
import springboot.shoppingmall.authorization.domain.RefreshToken;
import springboot.shoppingmall.authorization.domain.RefreshTokenRepository;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.authorization.dto.LoginRequest;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenListRepository;

    public TokenResponse login(LoginRequest loginRequest, String accessIp) {
        User loginUser = getLoginUser(loginRequest);
        String accessToken = jwtTokenProvider.createAccessToken(loginUser, accessIp);
        String refreshToken = jwtTokenProvider.createRefreshToken(loginUser, accessIp);

        refreshTokenListRepository.save(new RefreshToken(loginUser, refreshToken));

        return new TokenResponse(accessToken, refreshToken);
    }

    public AuthorizedUser getAuthorizedUser(String token){
        if(!jwtTokenProvider.validateExpireToken(token)){
            throw new IllegalArgumentException();
        }

        Long userId = jwtTokenProvider.getUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);

        return new AuthorizedUser(user.getId(), user.getLoginId());
    }

    private User getLoginUser(LoginRequest loginRequest) {
        return userRepository.findUserByLoginId(loginRequest.getLoginId())
                .filter(user -> user.isEqualPassword(loginRequest.getPassword()))
                .orElseThrow(
                        () -> new IllegalArgumentException("회원 조회 실패")
                );
    }
}
