package springboot.shoppingmall.providers.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProviderTest {

    @Test
    @DisplayName("판매자 정보가 생성되는 시점에는 승인되지 않은 상태이다.")
    void create_test() {
        // given

        // when
        Provider provider = Provider.builder()
                .name("(주) 부실건설")
                .address("서울시 영등포구 당산동")
                .telNo("1577-6789")
                .corporateRegistrationNumber("110-23-44444")
                .loginId("poorArchitect")
                .password("1q2w3e4r!")
                .build();

        // then
        assertThat(provider.isApproved()).isFalse();
    }

    @Test
    @DisplayName("판매 자격을 승인한다.")
    void approve_test() {
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
        provider.approve();

        // then
        assertThat(provider.isApproved()).isTrue();
    }

    @Test
    @DisplayName("상품코드에 사용될 마지막 sequence 조회")
    void product_sequence() {
        // given
        Provider provider = Provider.builder()
                .name("(주) 부실건설")
                .address("서울시 영등포구 당산동")
                .telNo("1577-6789")
                .corporateRegistrationNumber("110-23-44444")
                .loginId("poorArchitect")
                .password("1q2w3e4r!")
                .build();
        assertThat(provider.getProductSequence()).isEqualTo(0);

        // when
        int lastSequence = provider.getLastSequence();

        // then
        assertThat(lastSequence).isEqualTo(1);
        assertThat(provider.getProductSequence()).isEqualTo(1);
    }

    @Test
    @DisplayName("판매처의 현재 상품코드 조회")
    void product_code() {
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
        String productCode = provider.generateProductCode();

        // then
        assertThat(productCode).isEqualTo("1102344444000001");
    }
}