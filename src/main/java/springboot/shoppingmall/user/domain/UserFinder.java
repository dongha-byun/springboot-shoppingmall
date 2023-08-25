package springboot.shoppingmall.user.domain;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFinder {

    private final UserRepository userRepository;

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자 조회 실패")
                );
    }

    public User findUserByLoginId(String email) {
        return userRepository.findUserByLoginInfoEmail(email)
                .orElseThrow(
                        () -> new IllegalArgumentException("회원 조회 실패")
                );
    }

    public List<User> findUserOverTheUserGrade(UserGrade userGrade) {
        return userRepository.findUserOverTheUserGrade(userGrade.overGrades());
    }
}
