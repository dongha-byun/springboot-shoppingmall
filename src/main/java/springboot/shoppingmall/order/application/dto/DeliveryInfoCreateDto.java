package springboot.shoppingmall.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeliveryInfoCreateDto {
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
}
