package springboot.shoppingmall.order.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.client.couponservice.CouponServiceClient;
import springboot.shoppingmall.client.payservice.PayServiceClient;
import springboot.shoppingmall.order.domain.OrderItem;

@RequiredArgsConstructor
@Component
public class OrderCanceledEventHandler {
    private final PayServiceClient payServiceClient;
    private final CouponServiceClient couponServiceClient;

    public void handle(OrderItem orderItem) {
        // 1번과 2번이 비동기로 처리될 방법?
        // Async 로 돌리자. public 으로 파서
        // 1개의 메세지를 발행하고 2개의 consumer 가 가져갈 수 있나?

        // 여기서 두 가지 이벤트가 발생해야함. 비동기로 - 일단 동기로 개발해보기
        // 1. PG사 결제 취소 요청
        payServiceClient.cancel(orderItem.getOrder().getId(), orderItem.getRealPayPrice());

        // 2. 쿠폰 미사용 처리
        // spring domain event 를 적용해서 AbstractAggregateRoot 를 쓰려했지만, 추상클래스라 entity 에 사용할 수 가 없다.
        // 이미 BaseEntity 가 들어가 있기 때문. 차라리 다른 비동기 이벤트 방식을 써야겠는데, 만만한게 카프카 ?
        // 여기선 카프카 이벤트를 생성하고 보내는 식으로 해보자, 어차피 couponId 만 넘어가면 됨
        // eventPublisher -> eventListener
        couponServiceClient.recoveryCoupon(orderItem.getUsedUserCouponId());
    }
}
