package springboot.shoppingmall.order.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderDeliveryInfoDto {
    private String receiverName;
    private String receiverPhoneNumber;
    private String address;
    private String detailAddress;
    private String zipCode;
    private String requestMessage;

    public static OrderDeliveryInfoDto of(OrderDeliveryInfo info) {
        return new OrderDeliveryInfoDto(
                info.getReceiverName(), info.getReceiverPhoneNumber(),
                info.getAddress(), info.getDetailAddress(), info.getZipCode(),
                info.getRequestMessage()
        );
    }
}
