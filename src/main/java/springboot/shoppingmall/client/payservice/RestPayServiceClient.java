package springboot.shoppingmall.client.payservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelResponse;
import springboot.shoppingmall.pay.web.PayRequest;

@RequiredArgsConstructor
@Component
public class RestPayServiceClient implements PayServiceClient{
    private final RestTemplate restTemplate;
    private static final String PAY_URL = "http://localhost:10000/pay/";

    @Override
    public void cancel(String tid, String payType, int cancelAmount) {
        KakaoPayCancelRequest kakaoPayCancelRequest = new KakaoPayCancelRequest(
                tid,
                cancelAmount,
                0
        );
        PayRequest<KakaoPayCancelRequest> payRequest = new PayRequest<>(payType, kakaoPayCancelRequest);

        String uri = PAY_URL + payType + "/cancel";
        restTemplate.postForObject(
                uri,
                payRequest,
                KakaoPayCancelResponse.class
        );
    }
}
