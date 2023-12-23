package springboot.shoppingmall.pay.type.kakakopay.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.pay.type.kakakopay.dto.approve.KakaoPayApproveResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.cancel.KakaoPayCancelResponseDto;
import springboot.shoppingmall.pay.type.kakakopay.dto.ready.KakaoPayReadyResponseDto;

@Component
public class KakaoPayClient {

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

    public KakaoPayClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public KakaoPayReadyResponseDto ready(MultiValueMap<String, Object> formData) {
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, createHeaders());

        return restTemplate.postForObject(
                READY_URL, request, KakaoPayReadyResponseDto.class
        );
    }

    public KakaoPayApproveResponseDto approve(MultiValueMap<String, Object> formData) {
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, createHeaders());

        return restTemplate.postForObject(
                APPROVE_URL, request, KakaoPayApproveResponseDto.class
        );
    }

    public KakaoPayCancelResponseDto cancel(MultiValueMap<String, Object> formData) {
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(formData, createHeaders());

        return restTemplate.postForObject(
                CANCEL_URL, request, KakaoPayCancelResponseDto.class
        );
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, ADMIN_KEY_TYPE + " " + ADMIN_KEY);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return headers;
    }
}
