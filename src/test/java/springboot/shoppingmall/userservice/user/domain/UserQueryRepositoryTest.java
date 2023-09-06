package springboot.shoppingmall.userservice.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserQueryRepositoryTest {

    @Autowired
    UserQueryRepository userQueryRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("이름과 연락처로 이메일 정보를 조회한다.")
    void find_email_by_name_and_telNo() {
        // given
        userRepository.save(
                User.builder()
                        .userName("사용자1")
                        .email("user1@test.com")
                        .password("user1!")
                        .telNo("010-1234-1234")
                        .signUpDate(LocalDateTime.of(2023, 8, 25, 12, 0, 0))
                        .build()
        );

        // when
        User findUser = userQueryRepository.findEmailOf("사용자1", "010-1234-1234");

        // then
        assertThat(findUser.getId()).isNotNull();
        assertThat(findUser.getEmail()).isEqualTo("user1@test.com");
        assertThat(findUser.getUserName()).isEqualTo("사용자1");
        assertThat(findUser.telNo()).isEqualTo("010-1234-1234");
    }

}