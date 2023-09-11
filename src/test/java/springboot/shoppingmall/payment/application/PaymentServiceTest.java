package springboot.shoppingmall.payment.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.payment.application.dto.PaymentCreateDto;
import springboot.shoppingmall.payment.domain.CardCompany;
import springboot.shoppingmall.payment.domain.PayType;
import springboot.shoppingmall.payment.application.dto.PaymentDto;

@Transactional
@SpringBootTest
class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    Long userId = 10L;

    @Test
    @DisplayName("결제수단을 추가한다.")
    void createPaymentTest(){
        // given
        PaymentCreateDto createDto = PaymentCreateDto.builder()
                .cardNo1("1234").cardNo2("2134").cardNo3("2341").cardNo4("2341")
                .expireMM("10").expireYY("32").cvc("333")
                .payType(PayType.CARD).cardCom(CardCompany.SH)
                .build();
        // when
        PaymentDto paymentDto = paymentService.createPayment(userId, createDto);

        // then
        assertThat(paymentDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("결제수단을 삭제한다.")
    void deletePaymentTest() {
        // given
        PaymentCreateDto createDto = PaymentCreateDto.builder()
                .cardNo1("1234").cardNo2("2134").cardNo3("2341").cardNo4("2341")
                .expireMM("10").expireYY("32").cvc("333")
                .payType(PayType.CARD).cardCom(CardCompany.SH)
                .build();
        PaymentDto paymentDto = paymentService.createPayment(userId, createDto);

        // when
        paymentService.deletePayment(userId, paymentDto.getId());

        // then
        List<PaymentDto> allPayments = paymentService.findAllPayments(userId);
        assertThat(allPayments).hasSize(0);
    }

    @Test
    @DisplayName("등록된 결제수단들을 모두 조회한다.")
    void find_all() {
        // given
        PaymentCreateDto createDto1 = PaymentCreateDto.builder()
                .cardNo1("1234").cardNo2("2134").cardNo3("2341").cardNo4("2341")
                .expireMM("10").expireYY("32").cvc("333")
                .payType(PayType.CARD).cardCom(CardCompany.SH)
                .build();
        PaymentDto paymentDto1 = paymentService.createPayment(userId, createDto1);
        PaymentCreateDto createDto2 = PaymentCreateDto.builder()
                .cardNo1("9871").cardNo2("2430").cardNo3("2099").cardNo4("2109")
                .expireMM("02").expireYY("21").cvc("311")
                .payType(PayType.CARD).cardCom(CardCompany.KB)
                .build();
        PaymentDto paymentDto2 = paymentService.createPayment(userId, createDto2);

        // when
        List<PaymentDto> allPayments = paymentService.findAllPayments(userId);

        // then
        assertThat(allPayments).hasSize(2)
                .extracting("id", "cardCompany")
                .containsExactly(
                        tuple(paymentDto1.getId(), CardCompany.SH),
                        tuple(paymentDto2.getId(), CardCompany.KB)
                );
    }
}