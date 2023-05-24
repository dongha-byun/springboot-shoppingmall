package springboot.shoppingmall.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderFinder {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new IllegalArgumentException("주문 정보 조회 실패")
                );
    }

    public OrderItem findOrderByInvoiceNumber(String invoiceNumber) {
        return orderItemRepository.findOrderItemByInvoiceNumber(invoiceNumber)
                .orElseThrow(
                        () -> new IllegalArgumentException("송장 번호가 존재하지 않습니다.")
                );
    }

    public OrderItem findOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(
                        () -> new IllegalArgumentException("주문 상품 정보 조회 실패")
                );
    }
}
