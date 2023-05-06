package springboot.shoppingmall.order.partners.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PartnersOrderQueryDto {
    private Long id;
    private String orderCode;
    private LocalDateTime orderDate;
    private String productCode;
    private String productName;
    private int quantity;
    private String orderUserName;
    private String orderUserTelNo;
    private String receiverName;
    private String address;
    private String detailAddress;
    private String requestMessage;
    private String invoiceNumber;
    private LocalDateTime orderCancelDate;

    @QueryProjection
    public PartnersOrderQueryDto(Long id, String orderCode, LocalDateTime orderDate, String productCode,
                                 String productName, int quantity, String orderUserName, String orderUserTelNo,
                                 String receiverName, String address, String detailAddress, String requestMessage) {
        this.id = id;
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.orderUserName = orderUserName;
        this.orderUserTelNo = orderUserTelNo;
        this.receiverName = receiverName;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;
    }
}
