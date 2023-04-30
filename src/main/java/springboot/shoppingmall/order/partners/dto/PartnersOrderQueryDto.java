package springboot.shoppingmall.order.partners.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PartnersOrderQueryDto {
    private Long id;
    private LocalDateTime orderDate;
    private String productName;
    private String orderUserName;
    private String orderUserTelNo;
    private String receiverName;
    private String address;
    private String detailAddress;

    @QueryProjection
    public PartnersOrderQueryDto(Long id, LocalDateTime orderDate, String productName, String orderUserName,
                                 String orderUserTelNo, String receiverName, String address, String detailAddress) {
        this.id = id;
        this.orderDate = orderDate;
        this.productName = productName;
        this.orderUserName = orderUserName;
        this.orderUserTelNo = orderUserTelNo;
        this.receiverName = receiverName;
        this.address = address;
        this.detailAddress = detailAddress;
    }
}
