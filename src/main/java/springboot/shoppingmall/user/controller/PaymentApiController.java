package springboot.shoppingmall.user.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.user.dto.PaymentRequest;
import springboot.shoppingmall.user.dto.PaymentResponse;
import springboot.shoppingmall.user.service.PaymentService;

@RequiredArgsConstructor
@RestController
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> createPayment(@AuthenticationStrategy AuthorizedUser user,
                                                         @RequestBody PaymentRequest paymentRequest) {
        PaymentResponse paymentResponse = PaymentResponse.of(paymentService.createPayment(user.getId(), paymentRequest));
        return ResponseEntity.created(URI.create("/payments/"+paymentResponse.getId())).body(paymentResponse);
    }

    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentResponse> deletePayment(@AuthenticationStrategy AuthorizedUser user,
                                                         @PathVariable("paymentId") Long paymentId) {
        paymentService.deletePayment(user.getId(), paymentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponse>> findAllPayments(@AuthenticationStrategy AuthorizedUser user) {
        List<PaymentResponse> payments = paymentService.findAllPayments(user.getId()).stream()
                .map(PaymentResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(payments);
    }
}
