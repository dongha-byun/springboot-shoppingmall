package springboot.shoppingmall.payment.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.payment.application.dto.PaymentCreateDto;
import springboot.shoppingmall.payment.domain.Payment;
import springboot.shoppingmall.payment.domain.PaymentRepository;
import springboot.shoppingmall.payment.presentation.request.PaymentRequest;
import springboot.shoppingmall.payment.application.dto.PaymentDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentDto createPayment(Long userId, PaymentCreateDto createDto) {
        Payment payment = createDto.toEntity();
        Payment savedPayment = paymentRepository.save(payment.byUser(userId));

        return PaymentDto.of(savedPayment);
    }

    @Transactional
    public void deletePayment(Long userId, Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(
                        () -> new IllegalArgumentException("결제 수단이 존재하지 않습니다.")
                );

        if(payment.getUserId().equals(userId)) {
            paymentRepository.delete(payment);
        }
    }

    public List<PaymentDto> findAllPayments(Long userId) {
        List<Payment> payments = paymentRepository.findAll().stream()
                .filter(
                        payment -> payment.getUserId().equals(userId)
                ).collect(Collectors.toList());

        return payments.stream()
                .map(PaymentDto::of)
                .collect(Collectors.toList());
    }


}
