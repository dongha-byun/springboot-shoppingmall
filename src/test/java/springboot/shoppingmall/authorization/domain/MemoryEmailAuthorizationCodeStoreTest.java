package springboot.shoppingmall.authorization.domain;

import static org.assertj.core.api.Assertions.*;

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
}