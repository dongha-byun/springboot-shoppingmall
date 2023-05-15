package springboot.shoppingmall.order.partners.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import springboot.shoppingmall.order.domain.OrderStatus;

@NoArgsConstructor
@Getter
@Setter
public class PartnersEndOrderQueryDto extends PartnersOrderQueryDto{
    private String receiverName;        // 수령인
    private String address;             // 배송지 주소
    private String detailAddress;       // 배송지 상세주소
    private String requestMessage;      // 배송요청사항
    private String invoiceNumber;       // 송장번호
    private LocalDateTime deliveryDate; // 배송완료 시간
    private String deliveryPlace;       // 배송장소

    public PartnersEndOrderQueryDto(Long id, String orderCode, LocalDateTime orderDate,
                                    String productCode, String productName, int quantity, int totalPrice,
                                    String userName, String userTelNo, OrderStatus orderStatus,
                                    String receiverName, String address, String detailAddress,
                                    String requestMessage, String invoiceNumber,
                                    LocalDateTime deliveryDate, String deliveryPlace) {
        super(id, orderCode, orderDate, productCode, productName, quantity, totalPrice, userName, userTelNo,
                orderStatus);
        this.receiverName = receiverName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
        this.invoiceNumber = invoiceNumber;
        this.deliveryDate = deliveryDate;
        this.deliveryPlace = deliveryPlace;
    }
}
