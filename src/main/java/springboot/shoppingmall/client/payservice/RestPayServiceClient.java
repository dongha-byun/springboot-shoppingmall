package springboot.shoppingmall.client.payservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.pay.domain.PayHistory;
import springboot.shoppingmall.pay.domain.PayHistoryRepository;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelResponse;
import springboot.shoppingmall.pay.web.PayRequest;

@RequiredArgsConstructor
@Component
public class RestPayServiceClient implements PayServiceClient{
    private final PayHistoryRepository payHistoryRepository;
    private final RestTemplate restTemplate;
    private static final String PAY_URL = "http://localhost:10000/pay/";

    @Override
    public void cancel(Long orderId, int cancelAmount) {
        PayHistory payHistory = payHistoryRepository.findByOrderId(orderId)
                .orElseThrow(
                        () -> new IllegalArgumentException("거래내역 조회 실패")
                );

        KakaoPayCancelRequest kakaoPayCancelRequest = new KakaoPayCancelRequest(
                payHistory.getTid(),
                cancelAmount,
                0
        );
        PayRequest<KakaoPayCancelRequest> payRequest = new PayRequest<>(payHistory.getPayType().name(), kakaoPayCancelRequest);

        String uri = PAY_URL + payHistory.getPayType().name() + "/cancel";
        restTemplate.postForObject(
                uri,
                payRequest,
                KakaoPayCancelResponse.class
        );
    }
}
