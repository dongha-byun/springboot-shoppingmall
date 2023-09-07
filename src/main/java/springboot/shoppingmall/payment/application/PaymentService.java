package springboot.shoppingmall.payment.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.payment.domain.Payment;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserFinder;
import springboot.shoppingmall.payment.presentation.request.PaymentRequest;
import springboot.shoppingmall.payment.application.dto.PaymentDto;

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

    public List<PaymentDto> findAllPayments(Long userId) {
        User user = userFinder.findUserById(userId);
        List<Payment> payments = user.getPayments();

        return payments.stream()
                .map(PaymentDto::of)
                .collect(Collectors.toList());
    }


}
