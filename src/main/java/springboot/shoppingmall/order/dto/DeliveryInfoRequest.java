package springboot.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.application.dto.DeliveryInfoCreateDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfoRequest {
    private String receiverName;
    private String receiverPhoneNumber;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String requestMessage;

    public OrderDeliveryInfo toValue() {
        return new OrderDeliveryInfo(
                receiverName, receiverPhoneNumber, zipCode,
                address, detailAddress, requestMessage
        );
    }

    public DeliveryInfoCreateDto toDto() {
        return new DeliveryInfoCreateDto(
                receiverName, receiverPhoneNumber, zipCode,
                address, detailAddress, requestMessage
        );
    }
}
