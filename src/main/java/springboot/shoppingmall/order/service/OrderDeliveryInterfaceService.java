package springboot.shoppingmall.order.service;

import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;

public interface OrderDeliveryInterfaceService {
    OrderDeliveryInvoiceResponse createInvoiceNumber(Order order);
}
