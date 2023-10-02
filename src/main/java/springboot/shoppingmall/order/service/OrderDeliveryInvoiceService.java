package springboot.shoppingmall.order.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.dto.DeliveryEndRequest;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.service.dto.OrderItemDto;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderDeliveryInvoiceService {

    private final OrderFinder orderFinder;

    // 배송중 처리
    public OrderItemDto delivery(String invoiceNumber,
                                 LocalDateTime deliveryStartDate) {
        OrderItem orderItem = orderFinder.findOrderByInvoiceNumber(invoiceNumber);
        orderItem.delivery(deliveryStartDate);

        return OrderItemDto.of(orderItem);
    }

    // 배송완료 처리 - 택배사에서 호출하는 로직
    public OrderItemDto deliveryEnd(String invoiceNumber,
                                    LocalDateTime deliveryCompleteDate,
                                    String deliveryPlace) {
        OrderItem orderItem = orderFinder.findOrderByInvoiceNumber(invoiceNumber);
        orderItem.deliveryComplete(deliveryCompleteDate, deliveryPlace);

        return OrderItemDto.of(orderItem);
    }
}
