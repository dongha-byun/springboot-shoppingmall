package springboot.shoppingmall.partners.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class PartnerRepositoryTest {

    @Autowired
    PartnerRepository partnerRepository;

    @Test
    @DisplayName("판매자 등록 요청 테스트")
    void save_test() {
        // given
        Partner partner = Partner.builder()
                .name("(주) 부실건설")
                .address("서울시 영등포구 당산동")
                .telNo("1577-6789")
                .crn("110-23-44444")
                .email("poorArchitect@test.com")
                .password("1q2w3e4r!")
                .build();

        // when
        Partner savePartner = partnerRepository.save(partner);

        // then
        assertThat(savePartner.getId()).isNotNull();
        assertThat(savePartner.getName()).isEqualTo("(주) 부실건설");
        assertThat(savePartner.getAddress()).isEqualTo("서울시 영등포구 당산동");
        assertThat(savePartner.getTelNo()).isEqualTo("1577-6789");
        assertThat(savePartner.getCrn()).isEqualTo("110-23-44444");
        assertThat(savePartner.getEmail()).isEqualTo("poorArchitect@test.com");
        assertThat(savePartner.getPassword()).isEqualTo("1q2w3e4r!");
        assertThat(savePartner.isApproved()).isFalse();
    }

}