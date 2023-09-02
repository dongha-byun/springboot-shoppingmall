package springboot.shoppingmall.userservice.authentication.email.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemoryEmailAuthorizationCodeStoreTest {

    @Test
    @DisplayName("이메일에 발송된 인증번호를 저장한다.")
    void save_auth_code_of_email() {
        // given
        MemoryEmailAuthorizationCodeStore store = new MemoryEmailAuthorizationCodeStore();
        Email email = new Email("test@test.com");
        EmailAuthorizationCode code = new EmailAuthorizationCode("testCode");

        // when
        store.save(email, code);

        // then
        assertThat(store.getCode(new Email("test@test.com"))).isNotNull();
        assertThat(store.getCode(new Email("test@test.com"))).isEqualTo(new EmailAuthorizationCode("testCode"));
    }

    @Test
    @DisplayName("인증코드를 제거한다.")
    void remove_auth_code() {
        // given
        MemoryEmailAuthorizationCodeStore store = new MemoryEmailAuthorizationCodeStore();
        Email email = new Email("test@test.com");
        EmailAuthorizationCode code = new EmailAuthorizationCode("testCode");
        store.save(email, code);

        // when
        store.remove(new Email("test@test.com"));

        // then
        assertThatIllegalArgumentException().isThrownBy(
                () -> store.getCode(new Email("test@test.com"))
        );
    }

    @Test
    @DisplayName("인증코드를 저장할 때, 인증코드 만료시간도 같이 저장한다. 만료시간은 5분이다.")
    void save_auth_code_with_expire_time() {
        // given
        String emailAddress = "test@test.com";
        LocalDateTime requestTime = LocalDateTime.of(2023, 8, 20, 15, 0, 0);
        String authCode = "009344";

        Email email = new Email(emailAddress);
        EmailAuthorizationCode emailAuthCode = new EmailAuthorizationCode(authCode, requestTime);

        MemoryEmailAuthorizationCodeStore store = new MemoryEmailAuthorizationCodeStore();

        // when
        store.save(email, emailAuthCode);

        // then
        EmailAuthorizationCode findAuthCode = store.getCode(email);
        assertThat(findAuthCode.getValue()).isEqualTo("009344");
        assertThat(findAuthCode.getExpireTime()).isEqualTo(
                LocalDateTime.of(2023, 8, 20, 15, 5, 0)
        );
    }
}