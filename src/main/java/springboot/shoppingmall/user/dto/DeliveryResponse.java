package springboot.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.Delivery;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {
    private Long id;
    private String nickName;
    private String receiverName;
    private String receiverPhoneNumber;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String requestMessage;

    public static DeliveryResponse of(Delivery delivery){
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getNickName(),
                delivery.getReceiverName(),
                delivery.getReceiverPhoneNumber(),
                delivery.getZipCode(),
                delivery.getAddress(),
                delivery.getDetailAddress(),
                delivery.getRequestMessage()
        );
    }
}
