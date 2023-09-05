package springboot.shoppingmall.user.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.service.dto.SignUpRequestDto;

@Transactional
@SpringBootTest
class SignUpValidatorTest {

    @Autowired
    SignUpValidator signUpValidator;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원가입 시, 비밀번호와 비밀번호 확인 값이 다르면 회원가입에 실패한다.")
    void check_password_and_confirmPassword_same() {
        // given
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("신규 사용자")
                .email("new@test.com")
                .password("new1!")
                .confirmPassword("new1!!")
                .telNo("010-1234-1234")
                .signUpDate(LocalDateTime.now())
                .build();

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> signUpValidator.validateSignUp(signUpRequestDto)
        );
    }

    @Test
    @DisplayName("이미 가입된 이메일 정보가 있으면, 회원가입에 실패한다.")
    void check_exists_email() {
        // given
        userRepository.save(
                User.builder()
                        .userName("기존 사용자")
                        .email("new@test.com")
                        .password("new1!")
                        .telNo("010-1234-4444")
                        .signUpDate(LocalDateTime.of(2023, 8, 29, 12, 0, 0))
                        .build()
        );
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .name("신규 사용자")
                .email("new@test.com")
                .password("new1!")
                .confirmPassword("new1!")
                .telNo("010-1234-1234")
                .signUpDate(LocalDateTime.now())
                .build();

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> signUpValidator.validateSignUp(signUpRequestDto)
        );
    }
}