package springboot.shoppingmall.order.domain;

import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderDeliveryInfo {

    private String receiverName;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String requestMessage;

    public OrderDeliveryInfo(String receiverName, String zipCode, String address, String detailAddress,
                             String requestMessage) {
        this.receiverName = receiverName;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
    }
}
