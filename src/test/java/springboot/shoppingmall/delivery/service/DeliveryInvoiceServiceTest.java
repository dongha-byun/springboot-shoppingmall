package springboot.shoppingmall.delivery.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springboot.shoppingmall.delivery.dto.DeliveryInvoiceRequest;
import springboot.shoppingmall.delivery.dto.DeliveryInvoiceResponse;

class DeliveryInvoiceServiceTest {

    @Test
    @DisplayName("송장 번호 발급 테스트")
    void getDeliveryInvoiceTest(){
        // given
        String receiverName = "수령인";
        String receiverZipcode = "수령지 우편번호";
        String receiverAddress = "수령지 주소";
        String receiverDetailAddress = "수령지 상세주소";

        String providerName = "판매자";
        String providerZipcode = "판매지 우편번호";
        String providerAddress = "판매지 주소";
        String providerDetailAddress = "판매지 상세주소";
        DeliveryInvoiceRequest deliveryInvoiceRequest = new DeliveryInvoiceRequest(
                receiverName, receiverZipcode, receiverAddress, receiverDetailAddress,
                providerName, providerZipcode, providerAddress, providerDetailAddress
        );

        // when
        DeliveryInvoiceService deliveryInvoiceService = new SimpleDeliveryInvoiceService();
        DeliveryInvoiceResponse deliveryInvoice = deliveryInvoiceService.getDeliveryInvoice(deliveryInvoiceRequest);

        // then
        assertThat(deliveryInvoice.getInvoiceNumber()).isNotNull();
    }
}