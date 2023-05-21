package springboot.shoppingmall.order.partners.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderStatus;

@NoArgsConstructor
@Getter
public class PartnersDeliveryOrderQueryDto extends PartnersOrderQueryDto{
    private String receiverName;        // 수령인
    private String address;             // 배송지 주소
    private String detailAddress;       // 배송지 상세주소
    private String requestMessage;      // 배송요청사항

    public PartnersDeliveryOrderQueryDto(Long id, String orderCode, LocalDateTime orderDate,
                                         int totalPrice, String userName,
                                         String userTelNo, OrderStatus orderStatus,
                                         String receiverName, String address, String detailAddress,
                                         String requestMessage) {
        super(id, orderCode, orderDate, totalPrice, userName, userTelNo, orderStatus);
        this.receiverName = receiverName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
    }
}
