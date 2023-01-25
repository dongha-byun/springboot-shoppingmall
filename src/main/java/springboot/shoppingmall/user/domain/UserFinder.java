package springboot.shoppingmall.user.domain;

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
}
