package springboot.shoppingmall.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springboot.shoppingmall.payment.domain.CardCompany;
import springboot.shoppingmall.payment.domain.PayType;
import springboot.shoppingmall.payment.domain.Payment;
import springboot.shoppingmall.userservice.user.domain.User;

class PaymentTest {

    User user;

    @BeforeEach
    void beforeEach() {
        user = new User("테스트사용자", "testUser1", "testUser1!", "010-1234-1234");
    }

    @Test
    @DisplayName("결제수단 생성 테스트")
    void createTest() {
        // given

        // when
        Payment payment = new Payment(PayType.CARD, CardCompany.SH, "2134", "3456", "2341", "1232", "09", "27", "323")
                .byUser(user);

        // then
        assertThat(user.getPayments()).hasSize(1);
        assertThat(user.getPayments().get(0).getCardCom()).isSameAs(CardCompany.SH);
    }

    @Test
    @DisplayName("결제수단 삭제 테스트")
    void removeTest() {
        // given
        Payment payment1 = new Payment(100000L, PayType.CARD, CardCompany.SH, "2134", "3456", "2341", "1232", "09", "27", "323")
                .byUser(user);
        Payment payment2  = new Payment(100001L, PayType.CARD, CardCompany.SS, "1234", "1122", "3311", "2233", "10", "27", "112")
                .byUser(user);

        // when
        user.removePayment(payment1.getId());

        // then
        assertThat(user.getPayments()).hasSize(1);
    }

    @Test
    @DisplayName("결제수단 목록 조회 테스트")
    void findAllTest() {
        // given
        Payment payment1 = new Payment(PayType.CARD, CardCompany.SH, "2134", "3456", "2341", "1232", "09", "27", "323")
                .byUser(user);
        Payment payment2  = new Payment(PayType.CARD, CardCompany.SS, "1234", "1122", "3311", "2233", "10", "27", "112")
                .byUser(user);

        // when
        List<Payment> payments = user.getPayments();

        // then
        assertThat(payments).hasSize(2);
    }
}