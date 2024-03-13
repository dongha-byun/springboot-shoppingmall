package springboot.shoppingmall.order.partners.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.order.domain.OrderStatus;

@NoArgsConstructor
@Getter
@Setter
public class PartnersReadyOrderQueryDto extends PartnersOrderQueryDto{
    private String receiverName;        // 수령인
    private String receiverPhoneNumber; // 수령인 연락처
    private String address;             // 배송지 주소
    private String detailAddress;       // 배송지 상세주소
    private String requestMessage;      // 배송요청사항

    public PartnersReadyOrderQueryDto(Long orderItemId, Long orderId, String orderCode, LocalDateTime orderDate,
                                      String productCode, String productName, int quantity, String invoiceNumber,
                                      int totalPrice, Long userId, String userName, String userTelNo,
                                      OrderStatus orderStatus, String receiverName, String receiverPhoneNumber,
                                      String address, String detailAddress, String requestMessage) {
        super(orderItemId, orderId, orderCode, orderDate, productCode, productName, quantity, invoiceNumber, totalPrice,
                userId, userName, userTelNo, orderStatus);
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
    }

    public PartnersReadyOrderQueryDto(Long orderItemId, Long orderId, String orderCode, LocalDateTime orderDate,
                                      String productCode, String productName, int quantity, String invoiceNumber,
                                      int totalPrice, Long userId, OrderStatus orderStatus, String receiverName,
                                      String receiverPhoneNumber, String address, String detailAddress,
                                      String requestMessage) {
        this(orderItemId, orderId, orderCode, orderDate, productCode, productName, quantity, invoiceNumber, totalPrice,
                userId, null, null, orderStatus, receiverName, receiverPhoneNumber, address, detailAddress, requestMessage);
    }
}
