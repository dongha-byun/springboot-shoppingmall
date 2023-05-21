package springboot.shoppingmall.order.partners.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.partners.dto.PartnersOrderItemQueryDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersOrderQueryResponse {
    private Long id;
    private String orderCode;
    private String orderDate;
    private List<PartnersOrderItemQueryResponse> items;
    private int totalPrice;
    private String userName;
    private String userTelNo;
    private String orderStatusName;

    protected static List<PartnersOrderItemQueryResponse> orderItemDtoToResponse(
            List<PartnersOrderItemQueryDto> itemQueryDtos
    ) {
        return itemQueryDtos.stream()
                .map(PartnersOrderItemQueryResponse::to)
                .collect(Collectors.toList());
    }
}
