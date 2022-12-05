package springboot.shoppingmall.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.dto.LoginRequest;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public User login(LoginRequest loginRequest) {
        return userRepository.findUserByLoginId(loginRequest.getLoginId())
                .filter(user -> loginRequest.getPassword().equals(user.getPassword()))
                .orElseThrow(
                        () -> new IllegalArgumentException("로그인 실패")
                );
    }
}
