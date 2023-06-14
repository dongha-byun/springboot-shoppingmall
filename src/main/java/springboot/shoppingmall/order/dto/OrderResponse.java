package springboot.shoppingmall.order.dto;

import static springboot.shoppingmall.utils.DateUtils.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
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
    private DeliveryInfoResponse deliveryInfo;
    private OrderDeliveryInvoiceResponse deliveryInvoice;

    public static OrderResponse of(Order order) {
        OrderDeliveryInfo orderDeliveryInfo = order.getOrderDeliveryInfo();
        return new OrderResponse(order.getId(), order.getOrderCode(),
                toStringOfLocalDateTIme(order.getOrderDate()),
                ofItemList(order), order.getTotalPrice(),
                new DeliveryInfoResponse(
                        orderDeliveryInfo.getReceiverName(), orderDeliveryInfo.getReceiverPhoneNumber(),
                        orderDeliveryInfo.getZipCode(), orderDeliveryInfo.getAddress(),
                        orderDeliveryInfo.getDetailAddress(), orderDeliveryInfo.getRequestMessage()
                ),
                null);
    }

    public static OrderResponse of(Order order, OrderDeliveryInvoiceResponse deliveryInvoice) {
        OrderDeliveryInfo orderDeliveryInfo = order.getOrderDeliveryInfo();
        return new OrderResponse(order.getId(), order.getOrderCode(),
                toStringOfLocalDateTIme(order.getOrderDate()),
                ofItemList(order), order.getTotalPrice(),
                new DeliveryInfoResponse(
                        orderDeliveryInfo.getReceiverName(), orderDeliveryInfo.getReceiverPhoneNumber(),
                        orderDeliveryInfo.getZipCode(), orderDeliveryInfo.getAddress(),
                        orderDeliveryInfo.getDetailAddress(), orderDeliveryInfo.getRequestMessage()
                ),
                deliveryInvoice);
    }

    private static List<OrderItemResponse> ofItemList(Order order) {
        List<OrderItem> items = order.getItems();
        return items.stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList());
    }
}
