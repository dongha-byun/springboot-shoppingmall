package springboot.shoppingmall.order.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.OrderCodeCreator;
import springboot.shoppingmall.order.application.dto.ExchangeEndResultDto;
import springboot.shoppingmall.order.application.dto.OrderDto;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.handler.OrderCanceledEventHandler;
import springboot.shoppingmall.product.domain.Product;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderStatusChangeService {
    private final OrderFinder orderFinder;
    private final OrderDeliveryInterfaceService orderDeliveryInterfaceService;
    private final OrderUserInterfaceService orderUserInterfaceService;
    private final OrderCanceledEventHandler orderCanceledEventHandler;
    private final OrderRepository orderRepository;
    private final OrderCodeCreator orderCodeCreator;

    // 출고 중 - 파트너
    public OrderItemDto outing(Long orderItemId) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);

        OrderDeliveryInvoiceResponse deliveryInvoice = orderDeliveryInterfaceService.createInvoiceNumber(orderItem.getOrder());
        orderItem.outing(deliveryInvoice.getInvoiceNumber());

        return OrderItemDto.of(orderItem);
    }

    // 구매확정 - 사용자
    public OrderItemDto finish(Long orderItemId) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);
        orderItem.finish();

        // 구매확정 시, 사용자의 주문정보를 업데이트한다.
        Order order = orderItem.getOrder();
        orderUserInterfaceService.increaseOrderAmounts(order.getUserId(), orderItem.totalPrice());

        return OrderItemDto.of(orderItem);
    }

    // 검수중 - 파트너
    public OrderItemDto checking(Long orderItemId) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);
        orderItem.checking();

        return OrderItemDto.of(orderItem);
    }

    // 환불 완료 - 파트너
    public OrderItemDto refundEnd(Long orderItemId) {
        OrderItem orderItem = orderFinder.findOrderItemById(orderItemId);
        orderItem.refundEnd();

        // 결제 취소 로직 호출
        orderCanceledEventHandler.handle(orderItem);

        return OrderItemDto.of(orderItem);
    }

    public ExchangeEndResultDto exchangeEnd(Long orderItemId) {
        OrderItem exchangedOrderItem = orderFinder.findOrderItemById(orderItemId);
        Product product = exchangedOrderItem.getProduct();
        if(product.getStock() < exchangedOrderItem.getQuantity()) {
            // 교환 불가
            throw new IllegalArgumentException("교환 가능한 상품의 재고가 부족합니다. 구매자 분께 연락을 취하세요.");
        }
        exchangedOrderItem.exchangeEnd(); // 교환완료 처리 먼저 하고(재고수량 이슈만 아니면 교환은 가능하니까)

        // 원하는 결과물
        // 1. 주문번호가 다른 별도의 주문건
        // 2. 교환상품의 주문내역과 교환요청한 주문내역을 서로 오갈 수 있어야함
        // 그럼 테스트 코드를 어떻게 짜야될까?
        // 해당 메서드는 교환완료 처리"만" 한다고 생각하면
        //
        // 일단, 해당 메서드는 어떤 일을 해야할까?
        // 그냥 교환완료 처리만 하면 될까?
        // 아니면 새 주문정보를 같이생성해야할까?
        // 쿠폰 일단 그냥 똑같이 넣는다고 가정하면?
        // 일단 교환완료처리한 orderItem 정보를 return 하고,
        // 추가로 새로 생성된 교환상품 주문내역도 return 하는게 맞을지도
        // 일단 기존 orderItemDto 랑 새로운 orderDto 를 가지는 Dto 를 하나 만들어서 얘를 return 해보자
        // tid 어캄 -> 그냥 직접 save 쳐버리자
        // 하나 빠진게 있다.
        // 최초 주문이랑 새 주문이랑 이어주는 역할이 없다.
        // 그럼 이전 주문서 / 교환 주문서를 서로 확인할 수가 없는디
        // 중간에 다리하나 놔야하나?

        Order originOrder = exchangedOrderItem.getOrder();
        String newOrderCode = orderCodeCreator.createOrderCode();
        OrderItem newOrderItem = OrderItem.createOrderItem(
                product, exchangedOrderItem.getQuantity(), exchangedOrderItem.getUsedUserCouponId()
        );
        Order newOrder = orderRepository.save(
                new Order(
                        newOrderCode, originOrder.getUserId(), List.of(newOrderItem),
                        originOrder.getOrderDeliveryInfo()
                )
        );

        outing(newOrderItem.getId());

        return new ExchangeEndResultDto(
                OrderItemDto.of(exchangedOrderItem),
                OrderDto.of(newOrder)
        );
    }
}
