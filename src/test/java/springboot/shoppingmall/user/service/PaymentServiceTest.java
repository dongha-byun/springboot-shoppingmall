package springboot.shoppingmall.user.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.Payment;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.dto.PaymentRequest;

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
    @DisplayName("결제수단 추가")
    void createPaymentTest(){
        // given
        PaymentRequest paymentRequest = new PaymentRequest("1234", "2134", "3214", "2341", "10", "32", "333", "CREDIT_CARD","SS");

        // when
        Payment payment = paymentService.createPayment(user.getId(), paymentRequest);

        em.flush();
        em.clear();

        // then
        assertThat(payment.getId()).isNotNull();
    }

    @Test
    @DisplayName("결제수단 삭제")
    void deletePaymentTest() {
        // given
        PaymentRequest paymentRequest = new PaymentRequest("1234", "2134", "3214", "2341", "10", "32", "333", "CREDIT_CARD","SS");
        Payment payment = paymentService.createPayment(user.getId(), paymentRequest);

        em.flush();
        em.clear();

        // when
        paymentService.deletePayment(user.getId(), payment.getId());

        em.flush();
        em.clear();

        // then
        User findUser = userRepository.findById(user.getId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("사용자 조회 실패")
                        );
        assertThat(findUser.getPayments()).isEmpty();
    }

    @Test
    @DisplayName("결제수단 목록 조회 테스트")
    void findAllTest() {
        // given
        PaymentRequest paymentRequest1 = new PaymentRequest("1234", "2134", "3214", "2341", "10", "32", "333", "CREDIT_CARD","SS");
        PaymentRequest paymentRequest2 = new PaymentRequest("2111", "2134", "3214", "2341", "10", "33", "232", "CREDIT_CARD","SS");
        Payment payment1 = paymentService.createPayment(user.getId(), paymentRequest1);
        Payment payment2 = paymentService.createPayment(user.getId(), paymentRequest2);

        em.flush();
        em.clear();

        // when
        List<Payment> payments = paymentService.findAllPayments(user.getId());

        // then
        assertThat(payments).hasSize(2);
        List<Long> paymentIds = payments.stream()
                .map(Payment::getId).collect(Collectors.toList());

        assertThat(paymentIds).containsExactly(
                payment1.getId(), payment2.getId()
        );
    }
}