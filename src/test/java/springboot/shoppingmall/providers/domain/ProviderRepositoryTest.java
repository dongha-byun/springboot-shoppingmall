package springboot.shoppingmall.providers.domain;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ProviderRepositoryTest {

    @Autowired
    ProviderRepository providerRepository;

    @Test
    @DisplayName("판매자 등록 요청 테스트")
    void save_test() {
        // given
        Provider provider = Provider.builder()
                .name("(주) 부실건설")
                .address("서울시 영등포구 당산동")
                .telNo("1577-6789")
                .corporateRegistrationNumber("110-23-44444")
                .loginId("poorArchitect")
                .password("1q2w3e4r!")
                .build();

        // when
        Provider saveProvider = providerRepository.save(provider);

        // then
        assertThat(saveProvider.getId()).isNotNull();
        assertThat(saveProvider.getName()).isEqualTo("(주) 부실건설");
        assertThat(saveProvider.getAddress()).isEqualTo("서울시 영등포구 당산동");
        assertThat(saveProvider.getTelNo()).isEqualTo("1577-6789");
        assertThat(saveProvider.getCorporateRegistrationNumber()).isEqualTo("110-23-44444");
        assertThat(saveProvider.getLoginId()).isEqualTo("poorArchitect");
        assertThat(saveProvider.getPassword()).isEqualTo("1q2w3e4r!");
        assertThat(saveProvider.isApproved()).isFalse();
    }

}