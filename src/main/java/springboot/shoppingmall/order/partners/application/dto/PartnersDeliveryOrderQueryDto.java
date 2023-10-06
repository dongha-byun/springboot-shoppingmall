package springboot.shoppingmall.order.partners.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartnersDeliveryOrderQueryDto {
    private Long orderItemId;
    private Long orderId;
    private String orderCode;
    private LocalDateTime orderDate;
    private String productCode;
    private String productName;
    private int quantity;
    private String invoiceNumber;
    private int totalPrice;
    private Long userId;
    private String userName;
    private String userTelNo;
    private OrderStatus orderStatus;
    private String receiverName;        // 수령인
    private String receiverPhoneNumber; // 수령인 연락처
    private String address;             // 배송지 주소
    private String detailAddress;       // 배송지 상세주소
    private String requestMessage;      // 배송요청사항

    public PartnersDeliveryOrderQueryDto(Long orderItemId, Long orderId, String orderCode, LocalDateTime orderDate,
                                         String productCode, String productName, int quantity, String invoiceNumber,
                                         int totalPrice, Long userId, OrderStatus orderStatus, String receiverName,
                                         String receiverPhoneNumber, String address, String detailAddress,
                                         String requestMessage) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.invoiceNumber = invoiceNumber;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
    }
}
