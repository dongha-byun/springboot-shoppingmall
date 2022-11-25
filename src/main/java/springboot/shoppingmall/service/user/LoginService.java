package springboot.shoppingmall.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.dto.user.LoginRequest;
import springboot.shoppingmall.dto.user.LoginResponse;
import springboot.shoppingmall.repository.user.UserRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        return LoginResponse.of(userRepository.findUserByLoginId(loginRequest.getLoginId())
                .filter(user -> loginRequest.getPassword().equals(user.getPassword()))
                .orElseThrow(
                        () -> new IllegalArgumentException("로그인 실패")
                ));
    }
}
