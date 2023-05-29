package springboot.shoppingmall.order.domain;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderDeliveryInfo {

    @Embedded
    private Receiver receiver;

    @Embedded
    private Address address;
    private String requestMessage;

    public OrderDeliveryInfo(String receiverName, String receiverPhoneNumber,
                             String zipCode, String address, String detailAddress,
                             String requestMessage) {
        this.receiver = new Receiver(receiverName, receiverPhoneNumber);
        this.address = new Address(zipCode, address, detailAddress);
        this.requestMessage = requestMessage;
    }

    public String getReceiverName() {
        return this.receiver.getName();
    }

    public String getReceiverPhoneNumber() {
        return this.receiver.getPhoneNumber();
    }

    public String getAddress() {
        return this.address.getAddress();
    }

    public String getDetailAddress() {
        return this.address.getDetailAddress();
    }

    public String getZipCode() {
        return this.address.getZipCode();
    }
}
