package springboot.shoppingmall.order.partners.controller;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersOrderQueryResponse {
    private Long id;
    private LocalDateTime orderDate;
    private String productName;
    private String orderUserName;
    private String orderUserTelNo;
    private String receiverName;
    private String address;
    private String detailAddress;

    public static PartnersOrderQueryResponse to(PartnersOrderQueryDto dto) {
        return new PartnersOrderQueryResponse(
                dto.getId(), dto.getOrderDate(), dto.getProductName(), dto.getOrderUserName(),
                dto.getOrderUserTelNo(), dto.getReceiverName(), dto.getAddress(), dto.getDetailAddress()
        );
    }
}
