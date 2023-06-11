package springboot.shoppingmall.pay.type.kakakopay.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.pay.service.PayService;
import springboot.shoppingmall.pay.type.kakakopay.web.KakaoPayApproveResponse;
import springboot.shoppingmall.pay.type.kakakopay.web.KakaoPayCancelResponse;
import springboot.shoppingmall.pay.type.kakakopay.web.KakaoPayReadyResponse;

public class KakaoPayService implements PayService {

    private final RestTemplate restTemplate;
    private static final String READY_URL = "https://kapi.kakao.com/v1/payment/ready";
    private static final String APPROVE_URL = "https://kapi.kakao.com/v1/payment/approve";
    private static final String CANCEL_URL = "https://kapi.kakao.com/v1/payment/cancel";

    private static final String ADMIN_KEY_TYPE = "KakaoAK";
    private static final String ADMIN_KEY = "22f748186772959eb46af0f3e0773131";

    public KakaoPayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Object ready(MultiValueMap<String, String> formData) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, createHeaders());

        return restTemplate.postForObject(
                READY_URL, request, KakaoPayReadyResponse.class
        );
    }

    @Override
    public Object approve(MultiValueMap<String, String> formData) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, createHeaders());

        return restTemplate.postForObject(
                APPROVE_URL, request, KakaoPayApproveResponse.class
        );
    }

    @Override
    public Object cancel(MultiValueMap<String, Object> formData) {
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, createHeaders());

        return restTemplate.postForObject(
                CANCEL_URL, request, KakaoPayCancelResponse.class
        );
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, ADMIN_KEY_TYPE + " " + ADMIN_KEY);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return headers;
    }
}
