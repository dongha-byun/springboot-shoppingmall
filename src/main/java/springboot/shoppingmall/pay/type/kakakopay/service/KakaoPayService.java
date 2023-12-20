package springboot.shoppingmall.pay.type.kakakopay.service;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${pay.api.kakao.ready_url}")
    private String READY_URL;

    @Value("${pay.api.kakao.approve_url}")
    private String APPROVE_URL;

    @Value("${pay.api.kakao.cancel_url}")
    private String CANCEL_URL;

    @Value("${pay.api.kakao.type}")
    private String ADMIN_KEY_TYPE;

    @Value("${pay.api.kakao.key}")
    private String ADMIN_KEY;

    public KakaoPayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Object ready(MultiValueMap<String, Object> formData) {
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, createHeaders());

        return restTemplate.postForObject(
                READY_URL, request, KakaoPayReadyResponse.class
        );
    }

    @Override
    public Object approve(MultiValueMap<String, Object> formData) {
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, createHeaders());

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
