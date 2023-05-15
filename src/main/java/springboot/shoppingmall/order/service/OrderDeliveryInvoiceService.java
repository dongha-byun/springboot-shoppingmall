package springboot.shoppingmall.order.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.dto.DeliveryEndRequest;
import springboot.shoppingmall.order.dto.OrderResponse;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderDeliveryInvoiceService {

    private final OrderFinder orderFinder;

    // 배송중 처리
    public OrderResponse delivery(String invoiceNumber) {
        Order order = orderFinder.findOrderByInvoiceNumber(invoiceNumber);
        order.delivery();

        return OrderResponse.of(order);
    }

    // 배송완료 처리 - 택배사에서 호출하는 로직
    public OrderResponse deliveryEnd(String invoiceNumber, LocalDateTime deliveryEndDate, String deliveryPlace) {
        Order order = orderFinder.findOrderByInvoiceNumber(invoiceNumber);
        order.deliveryEnd(deliveryEndDate, deliveryPlace);

        return OrderResponse.of(order);
    }
}
