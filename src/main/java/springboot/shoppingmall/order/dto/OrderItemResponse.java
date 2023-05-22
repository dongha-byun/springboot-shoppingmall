package springboot.shoppingmall.order.dto;

import static springboot.shoppingmall.utils.DateUtils.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.utils.DateUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private String invoiceNumber;
    private String orderStatusName;
    private String cancelDate;
    private String cancelReason;

    public static OrderItemResponse of(OrderItem orderItem) {
        Product product = orderItem.getProduct();
        return new OrderItemResponse(orderItem.getId(), product.getId(),
                product.getName(), orderItem.getQuantity(), orderItem.getInvoiceNumber(),
                orderItem.getOrderStatus().getStatusName(),
                toStringOfLocalDateTIme(orderItem.getCancelDate()), orderItem.getCancelReason());
    }

}
