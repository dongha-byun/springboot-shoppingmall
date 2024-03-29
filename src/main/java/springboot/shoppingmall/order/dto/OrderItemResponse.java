package springboot.shoppingmall.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.application.dto.OrderItemDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private int quantity;
    private int totalPrice;
    private Long usedCouponId;
    private int couponDiscountAmount;
    private int gradeDiscountAmount;
    private String invoiceNumber;
    private String orderStatusName;

    public static OrderItemResponse of(OrderItemDto dto) {
        return new OrderItemResponse(dto.getId(),
                dto.getProductId(), dto.getQuantity(), dto.getTotalPrice(),
                dto.getUsedCouponId(), dto.getCouponDiscountAmount(), dto.getGradeDiscountAmount(),
                dto.getInvoiceNumber(), dto.getOrderStatus().getStatusName());
    }

}
