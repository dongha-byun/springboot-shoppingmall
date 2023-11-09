package springboot.shoppingmall.partners.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerRepository;
import springboot.shoppingmall.partners.dto.PartnerDto;

@Transactional
@SpringBootTest
class PartnerLoginServiceTest {

    @Autowired
    PartnerLoginService loginService;

    @Autowired
    PartnerRepository partnerRepository;

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

        // when
        PartnerDto partnerDto = loginService.login(loginId, "1q2w3e4r!");

        // then
        assertThat(partnerDto.getId()).isNotNull();
        assertThat(partnerDto.getLoginId()).isEqualTo("provider1");
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