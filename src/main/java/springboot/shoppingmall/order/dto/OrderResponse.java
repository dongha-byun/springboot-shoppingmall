package springboot.shoppingmall.order.dto;

import static springboot.shoppingmall.utils.DateUtils.*;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.application.dto.OrderDeliveryInfoDto;
import springboot.shoppingmall.order.application.dto.OrderDto;
import springboot.shoppingmall.order.application.dto.OrderItemDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderResponse {
    private Long id;
    private String orderCode;
    private String orderDate;
    private List<OrderItemResponse> items;
    private int totalPrice;
    private int realPayPrice;
    private DeliveryInfoResponse deliveryInfo;
    private OrderDeliveryInvoiceResponse deliveryInvoice;

    public static OrderResponse of(OrderDto dto) {
        OrderDeliveryInfoDto orderDeliveryInfo = dto.getDeliveryInfo();
        return new OrderResponse(dto.getId(), dto.getOrderCode(),
                toStringOfLocalDateTIme(dto.getOrderDate()),
                ofItemList(dto), dto.getTotalPrice(), dto.getRealPayPrice(),
                new DeliveryInfoResponse(
                        orderDeliveryInfo.getReceiverName(), orderDeliveryInfo.getReceiverPhoneNumber(),
                        orderDeliveryInfo.getZipCode(), orderDeliveryInfo.getAddress(),
                        orderDeliveryInfo.getDetailAddress(), orderDeliveryInfo.getRequestMessage()
                ),
                null);
    }

    public static OrderResponse of(OrderDto dto, OrderDeliveryInvoiceResponse deliveryInvoice) {
        OrderDeliveryInfoDto orderDeliveryInfo = dto.getDeliveryInfo();
        return new OrderResponse(dto.getId(), dto.getOrderCode(),
                toStringOfLocalDateTIme(dto.getOrderDate()),
                ofItemList(dto), dto.getTotalPrice(), dto.getRealPayPrice(),
                new DeliveryInfoResponse(
                        orderDeliveryInfo.getReceiverName(), orderDeliveryInfo.getReceiverPhoneNumber(),
                        orderDeliveryInfo.getZipCode(), orderDeliveryInfo.getAddress(),
                        orderDeliveryInfo.getDetailAddress(), orderDeliveryInfo.getRequestMessage()
                ),
                deliveryInvoice);
    }

    private static List<OrderItemResponse> ofItemList(OrderDto order) {
        List<OrderItemDto> items = order.getItems();
        return items.stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList());
    }
}
