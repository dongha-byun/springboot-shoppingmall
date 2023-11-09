package springboot.shoppingmall.partners.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.partners.dto.PartnerDto;

@Transactional
@SpringBootTest
class PartnerServiceTest {

    @Autowired
    PartnerService partnerService;

    @Test
    @DisplayName("판매 자격 신청 등록 테스트")
    void create_test() {
        // given
        PartnerDto partnerDto = new PartnerDto(
                "(주)파산은행", "김사채", "110-44-66666", "070-4444-4989",
                "서울시 사채구 빚더미동", "cash_bank", "1q2w3e4r!"
        );

        // when
        PartnerDto provider = partnerService.createPartner(partnerDto);

        // then
        assertThat(provider.getId()).isNotNull();
        assertThat(provider.isApproved()).isFalse();
        assertThat(provider.getCreatedAt()).isNotNull();
        assertThat(provider.getName()).isEqualTo("(주)파산은행");
        assertThat(provider.getCeoName()).isEqualTo("김사채");
        assertThat(provider.getCorporateRegistrationNumber()).isEqualTo("110-44-66666");
        assertThat(provider.getTelNo()).isEqualTo("070-4444-4989");
        assertThat(provider.getAddress()).isEqualTo("서울시 사채구 빚더미동");
        assertThat(provider.getLoginId()).isEqualTo("cash_bank");
        assertThat(provider.getPassword()).isEqualTo("1q2w3e4r!");
    }
}