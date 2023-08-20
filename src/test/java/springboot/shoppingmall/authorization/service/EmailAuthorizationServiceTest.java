package springboot.shoppingmall.authorization.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.TestEmailAuthorizationConfig;
import springboot.shoppingmall.authorization.domain.AuthorizationCodeGenerator;
import springboot.shoppingmall.authorization.domain.Email;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationCode;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationCodeStore;
import springboot.shoppingmall.authorization.domain.EmailAuthorizationProcessor;

@Import({TestEmailAuthorizationConfig.class})
@Transactional
@SpringBootTest
class EmailAuthorizationServiceTest {

    @Autowired
    EmailAuthorizationCodeStore store;

    @Autowired
    EmailAuthorizationProcessor processor;

    @Autowired
    AuthorizationCodeGenerator codeGenerator;


    @Test
    @DisplayName("인증코드를 생성하고 이메일을 발송한다.")
    void create_code_and_send_mail() {
        // 이메일에 대해 인증코드를 생성하고 인증코드를 포함한 이메일을 발송한다.
        // 1. 인증코드가 잘 저장됐는지,
        // 2. 이메일 본문이 잘 만들어 졌는지 확인한다.
        // 3. 발송 자체는 확인할 수 가 없을거같으니... -> 발송 이력을 남기면 좋을듯 하다. 성공/실패 여부를 포함해서.
        // given
        String email = "test@test.com";
        EmailAuthorizationService service = new EmailAuthorizationService(store, processor, codeGenerator);

        // when
        service.createCode(new Email(email));

        // then
        // 1. 인증코드 저장 확인
        EmailAuthorizationCode code = store.getCode(new Email(email));
        assertThat(code).isNotNull();
        assertThat(code.getValue()).isEqualTo("012345");

        // 2. 이메일 발송 여부 확인 -> 성공/실패 저장으로
    }

    @Test
    @DisplayName("입력한 인증번호가 발급된 인증번호와 같으면 이메일 인증에 성공했다고 판단한다.")
    void check_auth_code() {
        // 1. 이메일에 대해 인증번호를 발급한다.
        // 2. 인증코드 비교 결과 확인
        // given
        String email = "authTest@test.com";
        EmailAuthorizationService service = new EmailAuthorizationService(store, processor, codeGenerator);

        Email emailForCreate = new Email(email);
        service.createCode(emailForCreate);

        // when
        Email emailForCheck = new Email(email);
        service.checkCode(emailForCheck, new EmailAuthorizationCode("012345"));

        // then
        assertThat(emailForCreate).isEqualTo(emailForCheck);
    }
}