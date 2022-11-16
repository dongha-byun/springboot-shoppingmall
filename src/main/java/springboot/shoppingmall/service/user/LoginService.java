package springboot.shoppingmall.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.repository.user.UserRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public User login(String loginId, String password) {
        return userRepository.findUserByLoginId(loginId)
                .filter(user -> password.equals(user.getPassword()))
                .orElseThrow(
                        () -> new IllegalArgumentException("로그인 실패")
                );
    }
}
