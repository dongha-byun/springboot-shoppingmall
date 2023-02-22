package springboot.shoppingmall.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.dto.OrderResponse;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderDeliveryInvoiceService {

    private final OrderFinder orderFinder;

    public OrderResponse delivery(String invoiceNumber) {
        Order order = orderFinder.findOrderByInvoiceNumber(invoiceNumber);
        order.delivery();

        return OrderResponse.of(order);
    }

    public OrderResponse deliveryEnd(String invoiceNumber) {
        Order order = orderFinder.findOrderByInvoiceNumber(invoiceNumber);
        order.deliveryEnd();

        return OrderResponse.of(order);
    }
}
