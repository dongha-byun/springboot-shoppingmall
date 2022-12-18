package springboot.shoppingmall.authorization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.authorization.JwtTokenProvider;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.authorization.dto.LoginRequest;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public TokenResponse login(LoginRequest loginRequest) {
        User findUser = userRepository.findUserByLoginId(loginRequest.getLoginId())
                .filter(user -> user.isEqualPassword(loginRequest.getPassword()))
                .orElseThrow(
                        () -> new IllegalArgumentException("회원 조회 실패")
                );
        String token = jwtTokenProvider.createToken(findUser);
        return new TokenResponse(token);
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
}
