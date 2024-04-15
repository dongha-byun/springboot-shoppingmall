package springboot.shoppingmall.pay.type.kakakopay.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.authorization.GatewayConstants;
import springboot.shoppingmall.order.application.dto.DeliveryInfoCreateDto;
import springboot.shoppingmall.order.application.dto.OrderCreateDto;

@RequiredArgsConstructor
@Component
public class RestOrderClient implements OrderClient{
    private final RestTemplate restTemplate;
    @Override
    public void order(Long userId, OrderCreateDto orderCreateDto) {
        List<OrderItemRequest> itemRequests = orderCreateDto.getItems().stream()
                .map(dto -> new OrderItemRequest(
                        dto.getProductId(),
                        dto.getQuantity(),
                        dto.getUsedCouponId()
                )).collect(Collectors.toList());

        DeliveryInfoCreateDto deliveryInfo = orderCreateDto.getDeliveryInfo();
        DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest(
                deliveryInfo.getReceiverName(),
                deliveryInfo.getReceiverPhoneNumber(),
                deliveryInfo.getZipCode(),
                deliveryInfo.getAddress(),
                deliveryInfo.getDetailAddress(),
                deliveryInfo.getRequestMessage()
        );

        OrderRequest orderRequest = new OrderRequest(
                orderCreateDto.getTid(),
                orderCreateDto.getOrderCode(),
                orderCreateDto.getPayType().name(),
                itemRequests,
                orderCreateDto.getDeliveryFee(),
                deliveryInfoRequest
        );

        RequestEntity<OrderRequest> requestEntity = RequestEntity
                .post("/orders")
                .header(GatewayConstants.GATEWAY_HEADER, userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderRequest);

        restTemplate.exchange(requestEntity, OrderResponse.class);
    }

    static class OrderResponse {

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class OrderRequest {
        private String tid;
        private String orderCode;
        private String payType;
        private List<OrderItemRequest> items;
        private int deliveryFee;
        private DeliveryInfoRequest deliveryInfoRequest;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class OrderItemRequest {
        private Long productId;
        private int quantity;
        private Long usedCouponId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class DeliveryInfoRequest {
        private String receiverName;
        private String receiverPhoneNumber;
        private String zipCode;
        private String address;
        private String detailAddress;
        private String requestMessage;
    }
}
