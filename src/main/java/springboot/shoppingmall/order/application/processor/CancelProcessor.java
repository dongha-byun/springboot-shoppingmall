package springboot.shoppingmall.order.application.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.client.couponservice.CouponServiceClient;
import springboot.shoppingmall.client.payservice.PayServiceClient;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.pay.domain.PayHistory;
import springboot.shoppingmall.pay.domain.PayHistoryRepository;

@RequiredArgsConstructor
@Component
public class CancelProcessor implements ResolutionProcessor {
    private final PayHistoryRepository payHistoryRepository;
    private final PayServiceClient payServiceClient;
    private final CouponServiceClient couponServiceClient;

    @Override
    public void doProcess(OrderItem orderItem) {
        orderItem.cancel();

        // 여기서 두 가지 이벤트가 발생해야함. 비동기로 - 일단 동기로 개발해보기
        // 1. PG사 결제 취소 요청
        PayHistory payHistory = payHistoryRepository.findByOrderId(orderItem.getOrder().getId())
                .orElseThrow(
                        () -> new IllegalArgumentException("거래내역 조회에 실패했습니다.")
                );
        payServiceClient.cancel(payHistory.getTid(), payHistory.getPayType().name(), orderItem.getRealPayPrice());

        // 2. 쿠폰 미사용 처리
        // spring domain event 를 적용해서 AbstractAggregateRoot 를 쓰려했지만, 추상클래스라 entity 에 사용할 수 가 없다.
        // 이미 BaseEntity 가 들어가 있기 때문. 차라리 다른 비동기 이벤트 방식을 써야겠는데, 만만한게 카프카 ?
        // 여기선 카프카 이벤트를 생성하고 보내는 식으로 해보자, 어차피 couponId 만 넘어가면 됨
        // eventPublisher -> eventListener
        couponServiceClient.recoveryCoupon(orderItem.getUsedUserCouponId());
    }
}
