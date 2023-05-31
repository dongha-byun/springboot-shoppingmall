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
    private String orderDate;
    private List<OrderItemResponse> items;
    private int totalPrice;
    private String receiverName;
    private String receiverPhoneNumber;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String requestMessage;
    private OrderDeliveryInvoiceResponse deliveryInvoice;

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getId(), order.getOrderCode(),
                toStringOfLocalDateTIme(order.getOrderDate()),
                ofItemList(order), order.getTotalPrice(),
                order.getReceiverName(), order.getReceiverPhoneNumber(),
                order.getZipCode(), order.getAddress(),
                order.getDetailAddress(), order.getRequestMessage(),
                null);
    }

    public static OrderResponse of(Order order, OrderDeliveryInvoiceResponse deliveryInvoice) {
        return new OrderResponse(order.getId(), order.getOrderCode(),
                toStringOfLocalDateTIme(order.getOrderDate()),
                ofItemList(order), order.getTotalPrice(),
                order.getReceiverName(), order.getReceiverPhoneNumber(),
                order.getZipCode(), order.getAddress(),
                order.getDetailAddress(), order.getRequestMessage(),
                deliveryInvoice);
    }

    private static List<OrderItemResponse> ofItemList(Order order) {
        List<OrderItem> items = order.getItems();
        return items.stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList());
    }
}
