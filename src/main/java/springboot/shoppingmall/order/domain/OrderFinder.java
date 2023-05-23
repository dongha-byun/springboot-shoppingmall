package springboot.shoppingmall.order.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderFinder {

    private final OrderRepository orderRepository;

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new IllegalArgumentException("주문 정보 조회 실패")
                );
    }

    public Order findOrderByInvoiceNumber(String invoiceNumber) {
        return null;
    }
}
