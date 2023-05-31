package springboot.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.Delivery;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {
    private String nickName;
    private String receiverName;
    private String receiverPhoneNumber;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String requestMessage;

    public static Delivery to(DeliveryRequest request){
        return Delivery.builder()
                .nickName(request.getNickName())
                .receiverName(request.getReceiverName())
                .receiverPhoneNumber(request.getReceiverPhoneNumber())
                .zipCode(request.getZipCode())
                .address(request.getAddress())
                .detailAddress(request.getDetailAddress())
                .requestMessage(request.getRequestMessage())
                .build();
    }
}
