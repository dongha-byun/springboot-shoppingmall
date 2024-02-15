package springboot.shoppingmall.partners.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class PartnerLoginRepositoryTest {

    @Autowired
    PartnerLoginRepository partnerLoginRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Test
    @DisplayName("판매자 정보 로그인으로 조회")
    void find_by_loginId(){
        // given
        String loginId = "provider1";
        Partner savePartner = partnerRepository.save(new Partner(
                "판매업체1", "판매자1", "판매시 반품동", "02-1112-3333",
                "111-33-443322", loginId, "1q2w3e4r!"
        ));

        // when
        Partner findPartner = partnerLoginRepository.findByEmailForLogin(loginId);

        // then
        assertThat(savePartner.getId()).isEqualTo(findPartner.getId());
    }

    @Test
    @DisplayName("로그인 정보가 없으면 예외가 발생한다.")
    void find_by_loginId_no_provider() {
        // given
        String notExistsLoginId = "notExistsLoginId";
        String loginId = "provider1";
        Partner savePartner = partnerRepository.save(new Partner(
                "판매업체1", "판매자1", "판매시 반품동", "02-1112-3333",
                "111-33-443322", loginId, "1q2w3e4r!"
        ));

        // when & then
        assertThatThrownBy(
                () -> partnerLoginRepository.findByEmailForLogin(notExistsLoginId)
        ).isInstanceOf(RuntimeException.class);
    }
}
