package springboot.shoppingmall.order.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderItemDto {
    private Long id;
    private Long productId;
    private int quantity;
    private int totalPrice;
    private int realPayPrice;
    private Long usedCouponId;
    private int couponDiscountAmount;
    private int gradeDiscountAmount;
    private String invoiceNumber;
    private LocalDateTime deliveryStartDate;
    private LocalDateTime deliveryCompleteDate;
    private String deliveryPlace;
    private OrderStatus orderStatus;

    public static OrderItemDto of(OrderItem item) {
        return new OrderItemDto(
                item.getId(), item.getProduct().getId(), item.getQuantity(),
                item.getTotalPrice(), item.getRealPayPrice(), item.getUsedUserCouponId(),
                item.getCouponDiscountAmount(), item.getGradeDiscountAmount(),
                item.getInvoiceNumber(), item.getDeliveryStartDate(), item.getDeliveryCompleteDate(),
                item.getDeliveryPlace(), item.getOrderStatus()
        );
    }
}
