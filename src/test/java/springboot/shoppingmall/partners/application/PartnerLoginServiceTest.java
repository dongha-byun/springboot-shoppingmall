package springboot.shoppingmall.partners.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.client.userservice.UserServiceClient;
import springboot.shoppingmall.client.userservice.response.ResponsePartnerAuthInfo;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerRepository;

@Transactional
@SpringBootTest
class PartnerLoginServiceTest {

    @Autowired
    PartnerLoginService loginService;

    @Autowired
    PartnerRepository partnerRepository;

    @MockBean
    UserServiceClient userServiceClient;

    @Test
    @DisplayName("판매 자격이 승인된 계정의 로그인 성공")
    void login_success() {
        // given
        String loginId = "partner1";
        Partner savePartner = partnerRepository.save(new Partner(
                "판매업체1", "판매자1", "판매시 반품동", "02-1112-3333",
                "111-33-443322", loginId, "1q2w3e4r!"
        ));
        savePartner.approve();

        when(userServiceClient.authPartner(any())).thenReturn(
                new ResponsePartnerAuthInfo("access-token")
        );

        // when
        String accessToken = loginService.login(loginId, "1q2w3e4r!");

        // then
        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("판매 자격이 승인되지 않은 계정은 로그인에 실패한다.")
    void login_fail_with_not_approve() {
        // given
        String loginId = "partner1";
        partnerRepository.save(new Partner(
                "판매업체1", "판매자1", "판매시 반품동", "02-1112-3333",
                "111-33-443322", loginId, "1q2w3e4r!"
        ));

        // when & then
        assertThatIllegalStateException().isThrownBy(
                () -> loginService.login(loginId, "1q2w3e4r!")
        );
    }

    @Test
    @DisplayName("판매 자격이 승인되었어도 비밀번호가 틀리면 로그인에 실패한다.")
    void login_fail_with_not_collect_password() {
        // given
        String loginId = "partner1";
        Partner partner = partnerRepository.save(new Partner(
                "판매업체1", "판매자1", "판매시 반품동", "02-1112-3333",
                "111-33-443322", loginId, "1q2w3e4r!"
        ));
        partner.approve();

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> loginService.login(loginId, "1q2w3e4r@")
        );
    }
}