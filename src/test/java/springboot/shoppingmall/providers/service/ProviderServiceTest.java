package springboot.shoppingmall.providers.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.providers.dto.ProviderDto;

@Transactional
@SpringBootTest
class ProviderServiceTest {

    @Autowired
    ProviderService providerService;

    @Test
    @DisplayName("판매 자격 신청 등록 테스트")
    void create_test() {
        // given
        ProviderDto providerDto = new ProviderDto(
                "(주)파산은행", "김사채", "110-44-66666", "070-4444-4989",
                "서울시 사채구 빚더미동", "cash_bank", "1q2w3e4r!"
        );

        // when
        ProviderDto provider = providerService.createProvider(providerDto);

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