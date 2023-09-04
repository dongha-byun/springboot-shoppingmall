package springboot.shoppingmall.userservice.authentication.email.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemoryEmailAuthenticationCodeStoreTest {

    @Test
    @DisplayName("이메일에 발송된 인증번호를 저장한다.")
    void save_auth_code_of_email() {
        // given
        MemoryEmailAuthenticationCodeStore store = new MemoryEmailAuthenticationCodeStore();
        Email email = new Email("test@test.com");
        EmailAuthenticationCode code = new EmailAuthenticationCode("testCode");

        // when
        store.save(email, code);

        // then
        assertThat(store.getCode(new Email("test@test.com"))).isNotNull();
        assertThat(store.getCode(new Email("test@test.com"))).isEqualTo(new EmailAuthenticationCode("testCode"));
    }

    @Test
    @DisplayName("인증코드를 제거한다.")
    void remove_auth_code() {
        // given
        MemoryEmailAuthenticationCodeStore store = new MemoryEmailAuthenticationCodeStore();
        Email email = new Email("test@test.com");
        EmailAuthenticationCode code = new EmailAuthenticationCode("testCode");
        store.save(email, code);

        // when
        store.remove(new Email("test@test.com"));

        // then
        EmailAuthenticationCode findCode = store.getCode(new Email("test@test.com"));
        assertThat(findCode).isNull();
    }

    @Test
    @DisplayName("인증코드를 저장할 때, 인증코드 만료시간도 같이 저장한다. 만료시간은 5분이다.")
    void save_auth_code_with_expire_time() {
        // given
        String emailAddress = "test@test.com";
        LocalDateTime requestTime = LocalDateTime.of(2023, 8, 20, 15, 0, 0);
        String authCode = "009344";

        Email email = new Email(emailAddress);
        EmailAuthenticationCode emailAuthCode = new EmailAuthenticationCode(authCode, requestTime);

        MemoryEmailAuthenticationCodeStore store = new MemoryEmailAuthenticationCodeStore();

        // when
        store.save(email, emailAuthCode);

        // then
        EmailAuthenticationCode findAuthCode = store.getCode(email);
        assertThat(findAuthCode.getValue()).isEqualTo("009344");
        assertThat(findAuthCode.getExpireTime()).isEqualTo(
                LocalDateTime.of(2023, 8, 20, 15, 5, 0)
        );
    }
}