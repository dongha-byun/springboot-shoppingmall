package springboot.shoppingmall.order.application;

import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;

public interface OrderDeliveryInterfaceService {
    OrderDeliveryInvoiceResponse createInvoiceNumber(Order order);
}
