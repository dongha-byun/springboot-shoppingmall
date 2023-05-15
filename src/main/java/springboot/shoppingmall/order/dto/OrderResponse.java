package springboot.shoppingmall.order.dto;

import static springboot.shoppingmall.utils.DateUtils.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.utils.DateUtils;

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
    private String deliveryDate;
    private String deliveryPlace;
    private String cancelDate;
    private String cancelReason;

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderCode(),
                order.getOrderStatus().getStatusName(),
                order.getProduct().getName(), order.getQuantity(), order.getTotalPrice(),
                order.getReceiverName(), order.getZipCode(), order.getAddress(),
                order.getDetailAddress(), order.getRequestMessage(), order.getInvoiceNumber(),
                null, toStringOfLocalDateTIme(order.getDeliveryDate()), order.getDeliveryPlace(),
                toStringOfLocalDateTIme(order.getCancelDate()), order.getCancelReason());
    }

    public static OrderResponse of(Order order, OrderDeliveryInvoiceResponse deliveryInvoice) {
        return new OrderResponse(order.getId(), order.getOrderCode(),
                order.getOrderStatus().getStatusName(),
                order.getProduct().getName(), order.getQuantity(), order.getTotalPrice(),
                order.getReceiverName(), order.getZipCode(), order.getAddress(),
                order.getDetailAddress(), order.getRequestMessage(), order.getInvoiceNumber(),
                deliveryInvoice, toStringOfLocalDateTIme(order.getDeliveryDate()), order.getDeliveryPlace(),
                toStringOfLocalDateTIme(order.getCancelDate()), order.getCancelReason());
    }
}
