package springboot.shoppingmall.order.application;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderItemResolutionHistory;
import springboot.shoppingmall.order.domain.OrderItemResolutionHistoryRepository;
import springboot.shoppingmall.order.domain.OrderItemResolutionType;
import springboot.shoppingmall.pay.domain.PayHistory;
import springboot.shoppingmall.pay.domain.PayHistoryRepository;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelRequest;
import springboot.shoppingmall.pay.type.kakakopay.web.cancel.KakaoPayCancelResponse;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderItemResolutionService {
    private final OrderItemResolutionHistoryRepository repository;
    private final OrderFinder orderFinder;
    private final PayHistoryRepository payHistoryRepository;
    private final RestTemplate restTemplate;

    public Long saveResolutionHistory(
            Long userId, Long orderItemId, OrderItemResolutionType resolutionType,
            LocalDateTime dateTime, String content
    ) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);

        switch (resolutionType) {
            case CANCEL:
                orderItem.cancel();
                break;
            case REFUND:
                orderItem.refund();
                break;
            case EXCHANGE:
                orderItem.exchange();
                break;
        }

        OrderItemResolutionHistory resolutionHistory = repository.save(
                new OrderItemResolutionHistory(
                        orderItem, resolutionType, dateTime, content
                )
        );

        // 여기서 두 가지 이벤트가 발생해야함. 비동기로 - 일단 동기로 개발해보기
        // 1. PG사 결제 취소 요청
        //cancelPayment(orderItem);

        // 2. 쿠폰 미사용 처리
        // kafka or spring domain event

        return resolutionHistory.getId();
    }

    private void cancelPayment(OrderItem orderItem) {
        Order order = orderItem.getOrder();
        PayHistory payHistory = payHistoryRepository.findByOrderId(order.getId())
                .orElseThrow(
                        () -> new IllegalArgumentException("거래내역 조회에 실패했습니다.")
                );

        KakaoPayCancelRequest kakaoPayCancelRequest = new KakaoPayCancelRequest(
                payHistory.getTid(),
                orderItem.getRealPayPrice(),
                0
        );

        restTemplate.postForObject(
                "/pay/" + payHistory.getPayType() + "/cancel",
                kakaoPayCancelRequest,
                KakaoPayCancelResponse.class
        );
    }

}
