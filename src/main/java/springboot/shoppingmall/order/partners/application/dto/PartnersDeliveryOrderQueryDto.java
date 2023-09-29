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
    private String userName;
    private String userTelNo;
    private OrderStatus orderStatus;
    private String receiverName;        // 수령인
    private String receiverPhoneNumber; // 수령인 연락처
    private String address;             // 배송지 주소
    private String detailAddress;       // 배송지 상세주소
    private String requestMessage;      // 배송요청사항
}
