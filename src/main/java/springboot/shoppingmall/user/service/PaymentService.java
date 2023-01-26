package springboot.shoppingmall.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.Payment;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;
import springboot.shoppingmall.user.dto.PaymentRequest;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PaymentService {
    private final UserFinder userFinder;

    @Transactional
    public Payment createPayment(Long userId, PaymentRequest paymentRequest) {
        User user = userFinder.findUserById(userId);
        return PaymentRequest.to(paymentRequest)
                .byUser(user);
    }

    @Transactional
    public void deletePayment(Long userId, Long paymentId) {
        User user = userFinder.findUserById(userId);
        user.removePayment(paymentId);
    }

    public List<Payment> findAllPayments(Long userId) {
        User user = userFinder.findUserById(userId);
        return user.getPayments();
    }
}
