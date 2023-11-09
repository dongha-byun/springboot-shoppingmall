package springboot.shoppingmall.partners.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PartnerTest {

    @Test
    @DisplayName("판매자 정보가 생성되는 시점에는 승인되지 않은 상태이다.")
    void create_test() {
        // given

        // when
        Partner partner = Partner.builder()
                .name("(주) 부실건설")
                .address("서울시 영등포구 당산동")
                .telNo("1577-6789")
                .corporateRegistrationNumber("110-23-44444")
                .loginId("poorArchitect")
                .password("1q2w3e4r!")
                .build();

        // then
        assertThat(partner.isApproved()).isFalse();
    }

    @Test
    @DisplayName("판매 자격을 승인한다.")
    void approve_test() {
        // given
        Partner partner = Partner.builder()
                .name("(주) 부실건설")
                .address("서울시 영등포구 당산동")
                .telNo("1577-6789")
                .corporateRegistrationNumber("110-23-44444")
                .loginId("poorArchitect")
                .password("1q2w3e4r!")
                .build();

        // when
        partner.approve();

        // then
        assertThat(partner.isApproved()).isTrue();
    }

    @Test
    @DisplayName("상품코드에 사용될 마지막 sequence 조회")
    void product_sequence() {
        // given
        Partner partner = Partner.builder()
                .name("(주) 부실건설")
                .address("서울시 영등포구 당산동")
                .telNo("1577-6789")
                .corporateRegistrationNumber("110-23-44444")
                .loginId("poorArchitect")
                .password("1q2w3e4r!")
                .build();
        assertThat(partner.getProductSequence()).isEqualTo(0);

        // when
        int lastSequence = partner.getLastSequence();

        // then
        assertThat(lastSequence).isEqualTo(1);
        assertThat(partner.getProductSequence()).isEqualTo(1);
    }

    @Test
    @DisplayName("판매처의 현재 상품코드 조회")
    void product_code() {
        // given
        Partner partner = Partner.builder()
                .name("(주) 부실건설")
                .address("서울시 영등포구 당산동")
                .telNo("1577-6789")
                .corporateRegistrationNumber("110-23-44444")
                .loginId("poorArchitect")
                .password("1q2w3e4r!")
                .build();

        // when
        String productCode = partner.generateProductCode();

        // then
        assertThat(productCode).isEqualTo("1102344444000001");
    }
}