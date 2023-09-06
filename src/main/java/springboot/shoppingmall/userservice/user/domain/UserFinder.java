package springboot.shoppingmall.userservice.user.domain;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserFinder {

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

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
        return userQueryRepository.findUserOverTheUserGrade(userGrade.overGrades());
    }

    public User findEmailOf(String name, String telNo) {
        return userQueryRepository.findEmailOf(name, telNo);
    }

    public User findUserOf(String name, String telNo, String email) {
        return userQueryRepository.findUserOf(name, telNo, email);
    }
}
