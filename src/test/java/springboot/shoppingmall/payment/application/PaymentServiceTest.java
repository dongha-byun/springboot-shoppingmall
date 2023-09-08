package springboot.shoppingmall.payment.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.payment.application.PaymentService;
import springboot.shoppingmall.payment.application.dto.PaymentCreateDto;
import springboot.shoppingmall.payment.domain.CardCompany;
import springboot.shoppingmall.payment.domain.PayType;
import springboot.shoppingmall.payment.domain.Payment;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserRepository;
import springboot.shoppingmall.payment.presentation.request.PaymentRequest;
import springboot.shoppingmall.payment.application.dto.PaymentDto;

@Transactional
@SpringBootTest
class PaymentServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    PaymentService paymentService;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(new User("결제수단테스트사용자", "paymentUser", "paymentUser1!", "010-1234-1234"));
    }

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
        PaymentDto paymentDto = paymentService.createPayment(user.getId(), createDto);

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
        PaymentDto paymentDto = paymentService.createPayment(user.getId(), createDto);

        // when
        paymentService.deletePayment(user.getId(), paymentDto.getId());

        // then
        List<PaymentDto> allPayments = paymentService.findAllPayments(user.getId());
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
        PaymentDto paymentDto1 = paymentService.createPayment(user.getId(), createDto1);
        PaymentCreateDto createDto2 = PaymentCreateDto.builder()
                .cardNo1("9871").cardNo2("2430").cardNo3("2099").cardNo4("2109")
                .expireMM("02").expireYY("21").cvc("311")
                .payType(PayType.CARD).cardCom(CardCompany.KB)
                .build();
        PaymentDto paymentDto2 = paymentService.createPayment(user.getId(), createDto2);

        // when
        List<PaymentDto> allPayments = paymentService.findAllPayments(user.getId());

        // then
        assertThat(allPayments).hasSize(2)
                .extracting("id", "cardCompany")
                .containsExactly(
                        tuple(paymentDto1.getId(), CardCompany.SH),
                        tuple(paymentDto2.getId(), CardCompany.KB)
                );
    }
}