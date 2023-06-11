package springboot.shoppingmall.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.pay.type.kakakopay.web.KakaoPayCancelRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.KakaoPayCancelResponse;
import springboot.shoppingmall.pay.web.PayRequest;

@RequiredArgsConstructor
@Service
public class OrderPayService {

    public static final String CID = "TC0ONETIME";
    private final RestTemplate restTemplate;

    public void cancel(String tid, Integer cancelAmount) {
        KakaoPayCancelRequest kakaoPayCancelRequest = new KakaoPayCancelRequest(
                CID, tid, cancelAmount, 0
        );
        PayRequest<KakaoPayCancelRequest> request = new PayRequest<>("KAKAO_PAY", kakaoPayCancelRequest);
        restTemplate.postForEntity(
                "http://localhost:10000/pay/cancel", request, KakaoPayCancelResponse.class
        );
    }
}
