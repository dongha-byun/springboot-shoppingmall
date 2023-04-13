package springboot.shoppingmall.pay.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class PayController {
    private final RestTemplate restTemplate;

    public PayController() {
        this.restTemplate = new RestTemplate();
    }

    @PostMapping("/pay/ready")
    public ResponseEntity<KakaoPayReadyResponse> readyPay(@RequestBody PayRequest<KakaoPayReadyRequest> param) {
        KakaoPayReadyRequest kakaoPayReadyRequest = param.getData();
        MultiValueMap<String, String> formData = kakaoPayReadyRequest.toFormData();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK 22f748186772959eb46af0f3e0773131");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        KakaoPayReadyResponse response = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready", request, KakaoPayReadyResponse.class
        );

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/pay/approve")
    public ResponseEntity<KakaoPayApproveResponse> approvePay(@RequestBody PayRequest<KakaoPayApproveRequest> param) {
        KakaoPayApproveRequest kakaoPayApproveRequest = param.getData();
        MultiValueMap<String, String> formData = kakaoPayApproveRequest.toFormData();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK 22f748186772959eb46af0f3e0773131");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);

        KakaoPayApproveResponse kakaoPayApproveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve", request, KakaoPayApproveResponse.class
        );

        return ResponseEntity.ok().body(kakaoPayApproveResponse);
    }

}
