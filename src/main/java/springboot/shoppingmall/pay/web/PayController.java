package springboot.shoppingmall.pay.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.pay.service.PayService;

@RequiredArgsConstructor
@Slf4j
@RestController
public class PayController {

    private final PayService payService;

    @PostMapping("/pay/ready")
    public ResponseEntity<KakaoPayReadyResponse> readyPay(@RequestBody PayRequest<KakaoPayReadyRequest> param) {
        KakaoPayReadyRequest kakaoPayReadyRequest = param.getData();
        MultiValueMap<String, String> formData = kakaoPayReadyRequest.toFormData();

        KakaoPayReadyResponse response = (KakaoPayReadyResponse) payService.ready(formData);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/pay/approve")
    public ResponseEntity<KakaoPayApproveResponse> approvePay(@RequestBody PayRequest<KakaoPayApproveRequest> param) {
        KakaoPayApproveRequest kakaoPayApproveRequest = param.getData();
        MultiValueMap<String, String> formData = kakaoPayApproveRequest.toFormData();

        KakaoPayApproveResponse response = (KakaoPayApproveResponse) payService.approve(formData);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/pay/cancel")
    public ResponseEntity<KakaoPayCancelResponse> cancelPay(@RequestBody PayRequest<KakaoPayCancelRequest> param) {
        KakaoPayCancelRequest kakaoPayCancelRequest = param.getData();
        MultiValueMap<String, Object> formData = kakaoPayCancelRequest.toFormData();

        KakaoPayCancelResponse response = (KakaoPayCancelResponse) payService.cancel(formData);

        return ResponseEntity.ok().body(response);
    }
}
