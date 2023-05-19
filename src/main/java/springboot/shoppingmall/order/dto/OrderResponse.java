package springboot.shoppingmall.order.dto;

import static springboot.shoppingmall.utils.DateUtils.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.utils.DateUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderResponse {
    private Long id;
    private String orderCode;
    private String orderStatusName;
    private List<OrderItemResponse> items;
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
                ofItemList(order), order.getTotalPrice(),
                order.getReceiverName(), order.getZipCode(), order.getAddress(),
                order.getDetailAddress(), order.getRequestMessage(), order.getInvoiceNumber(),
                null, toStringOfLocalDateTIme(order.getDeliveryDate()), order.getDeliveryPlace(),
                toStringOfLocalDateTIme(order.getCancelDate()), order.getCancelReason());
    }

    public static OrderResponse of(Order order, OrderDeliveryInvoiceResponse deliveryInvoice) {
        return new OrderResponse(order.getId(), order.getOrderCode(),
                order.getOrderStatus().getStatusName(),
                ofItemList(order), order.getTotalPrice(),
                order.getReceiverName(), order.getZipCode(), order.getAddress(),
                order.getDetailAddress(), order.getRequestMessage(), order.getInvoiceNumber(),
                deliveryInvoice, toStringOfLocalDateTIme(order.getDeliveryDate()), order.getDeliveryPlace(),
                toStringOfLocalDateTIme(order.getCancelDate()), order.getCancelReason());
    }

    private static List<OrderItemResponse> ofItemList(Order order) {
        List<OrderItem> items = order.getItems();
        return items.stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList());
    }
}
