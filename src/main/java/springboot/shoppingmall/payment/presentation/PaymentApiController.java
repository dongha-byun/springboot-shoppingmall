package springboot.shoppingmall.payment.presentation;

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
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.payment.application.dto.PaymentCreateDto;
import springboot.shoppingmall.payment.presentation.request.PaymentRequest;
import springboot.shoppingmall.payment.presentation.response.PaymentResponse;
import springboot.shoppingmall.payment.application.PaymentService;
import springboot.shoppingmall.payment.application.dto.PaymentDto;

@RequiredArgsConstructor
@RestController
public class PaymentApiController {

    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> createPayment(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                         @RequestBody PaymentRequest paymentRequest) {
        PaymentCreateDto createDto = paymentRequest.toDto();
        PaymentDto paymentDto = paymentService.createPayment(gatewayAuthInfo.getUserId(), createDto);
        PaymentResponse paymentResponse = PaymentResponse.of(paymentDto);
        return ResponseEntity.created(URI.create("/payments/"+paymentResponse.getId())).body(paymentResponse);
    }

    @DeleteMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentResponse> deletePayment(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                         @PathVariable("paymentId") Long paymentId) {
        paymentService.deletePayment(gatewayAuthInfo.getUserId(), paymentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponse>> findAllPayments(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo) {
        List<PaymentDto> payments = paymentService.findAllPayments(gatewayAuthInfo.getUserId());
        List<PaymentResponse> responses = payments.stream()
                .map(PaymentResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
