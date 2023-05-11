package springboot.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.Order;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderResponse {
    private Long id;
    private String orderCode;
    private String orderStatusName;
    private String productName;
    private int quantity;
    private int totalPrice;
    private String receiverName;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String requestMessage;
    private String invoiceNumber;
    private OrderDeliveryInvoiceResponse deliveryInvoice;


    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderCode(),
                order.getOrderStatus().getStatusName(),
                order.getProduct().getName(), order.getQuantity(), order.getTotalPrice(),
                order.getReceiverName(), order.getZipCode(), order.getAddress(),
                order.getDetailAddress(), order.getRequestMessage(), order.getInvoiceNumber(), null);
    }

    public static OrderResponse of(Order order, OrderDeliveryInvoiceResponse deliveryInvoice) {
        return new OrderResponse(order.getId(), order.getOrderCode(),
                order.getOrderStatus().getStatusName(),
                order.getProduct().getName(), order.getQuantity(), order.getTotalPrice(),
                order.getReceiverName(), order.getZipCode(), order.getAddress(),
                order.getDetailAddress(), order.getRequestMessage(), order.getInvoiceNumber(), deliveryInvoice);
    }
}
