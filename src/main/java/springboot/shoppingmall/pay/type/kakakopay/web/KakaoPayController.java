package springboot.shoppingmall.pay.type.kakakopay.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.pay.type.kakakopay.service.KakaoPayService;
import springboot.shoppingmall.pay.web.PayRequest;

@RequiredArgsConstructor
@RestController
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/pay/KAKAO_PAY/ready")
    public ResponseEntity<KakaoPayReadyResponse> readyPay(@RequestBody PayRequest<KakaoPayReadyRequest> param) {
        KakaoPayReadyRequest kakaoPayReadyRequest = param.getData();
        MultiValueMap<String, String> formData = kakaoPayReadyRequest.toFormData();

        KakaoPayReadyResponse response = (KakaoPayReadyResponse) kakaoPayService.ready(formData);

        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/pay/KAKAO_PAY/approve")
    public ResponseEntity<KakaoPayApproveResponse> approvePay(@RequestBody PayRequest<KakaoPayApproveRequest> param) {
        KakaoPayApproveRequest kakaoPayApproveRequest = param.getData();
        MultiValueMap<String, String> formData = kakaoPayApproveRequest.toFormData();

        KakaoPayApproveResponse response = (KakaoPayApproveResponse) kakaoPayService.approve(formData);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/pay/KAKAO_PAY/cancel")
    public ResponseEntity<KakaoPayCancelResponse> cancelPay(@RequestBody PayRequest<KakaoPayCancelRequest> param) {
        KakaoPayCancelRequest kakaoPayCancelRequest = param.getData();
        MultiValueMap<String, Object> formData = kakaoPayCancelRequest.toFormData();

        KakaoPayCancelResponse response = (KakaoPayCancelResponse) kakaoPayService.cancel(formData);

        return ResponseEntity.ok().body(response);
    }
}
