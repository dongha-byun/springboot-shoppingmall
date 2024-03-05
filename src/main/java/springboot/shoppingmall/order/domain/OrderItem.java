package springboot.shoppingmall.order.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import springboot.shoppingmall.BaseEntity;
import springboot.shoppingmall.product.domain.Product;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "order_item")
@Entity
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private int quantity;
    private int totalPrice;
    private int realPayPrice;
    private int gradeDiscountAmount;

    private Long usedUserCouponId;
    private int couponDiscountAmount;

    @Column(unique = true)
    private String invoiceNumber;
    private LocalDateTime deliveryStartDate;
    private LocalDateTime deliveryCompleteDate;
    private String deliveryPlace;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public static OrderItem createOrderItem(Product product, int quantity) {
        return createOrderItem(product, quantity, null);
    }
    public static OrderItem createOrderItem(Product product, int quantity, Long usedUserCouponId) {
        return new OrderItem(product, quantity, usedUserCouponId, OrderStatus.PREPARED);
    }

    public OrderItem(Product product, int quantity, OrderStatus orderStatus) {
        this(product, quantity, null, orderStatus);
    }
    public OrderItem(Product product, int quantity, Long usedUserCouponId, OrderStatus orderStatus) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = product.getPrice() * quantity;
        this.orderStatus = orderStatus;
        this.usedUserCouponId = usedUserCouponId;

        validateQuantity(quantity);

        // 상품 주문 정보 생성 시, 상품의 재고수량을 감소시킨다.
        removeQuantity();
    }

    private void validateQuantity(int quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException("상품은 1개 이상 주문해야 합니다.");
        }
        product.validateQuantity(quantity);
    }

    public void ordered(Order order){
        this.order = order;
    }

    public int totalPrice() {
        return this.totalPrice;
    }

    public void increaseQuantity() {
        product.increaseQuantity(quantity);
    }

    public void removeQuantity() {
        product.removeQuantity(quantity);
    }

    public void cancel() {
        if(OrderStatus.PREPARED != this.orderStatus) {
            throw new IllegalArgumentException("준비 중인 주문 만 취소가 가능합니다.");
        }
        this.orderStatus = OrderStatus.CANCEL;

        increaseQuantity();
    }

    public void outing(String invoiceNumber) {
        if(!StringUtils.hasText(invoiceNumber)) {
            throw new IllegalArgumentException("송장번호가 존재하지 않아, 출고중 처리가 불가합니다.");
        }
        if(OrderStatus.PREPARED != this.orderStatus) {
            throw new IllegalArgumentException("준비 중인 상품만 출고중 처리가 가능합니다.");
        }

        this.orderStatus = OrderStatus.OUTING;
        this.invoiceNumber = invoiceNumber;
    }

    public void delivery(LocalDateTime deliveryStartDate) {
        if(OrderStatus.OUTING != this.orderStatus) {
            throw new IllegalArgumentException("출고된 상품만 배송중 처리가 가능합니다.");
        }
        this.orderStatus = OrderStatus.DELIVERY;
        this.deliveryStartDate = deliveryStartDate;
    }

    public void deliveryComplete(LocalDateTime deliveryCompleteDate, String deliveryPlace) {
        if(this.deliveryStartDate.isAfter(deliveryCompleteDate)) {
            throw new IllegalArgumentException("배송 종료시간이 배송 시작시간보다 나중일 수 없습니다.");
        }
        if(OrderStatus.DELIVERY != this.orderStatus) {
            throw new IllegalArgumentException("배송중인 상품만 배송완료 처리가 가능합니다.");
        }
        this.deliveryCompleteDate = deliveryCompleteDate;
        this.deliveryPlace = deliveryPlace;
        this.orderStatus = OrderStatus.DELIVERY_END;
    }

    public void finish() {
        if(OrderStatus.DELIVERY_END != this.orderStatus) {
            throw new IllegalArgumentException("배송이 완료된 주문 만 구매확정이 가능합니다.");
        }
        this.orderStatus = OrderStatus.FINISH;

        // 구매 확정 시, 상품의 판매수량이 증가한다.
        product.increaseSalesVolume(this.quantity);
    }

    public void refund() {
        if(this.orderStatus != OrderStatus.DELIVERY_END) {
            throw new IllegalArgumentException("배송이 완료된 주문만 환불 신청이 가능합니다.");
        }
        this.orderStatus = OrderStatus.REFUND;
    }

    public void exchange() {
        if(this.orderStatus != OrderStatus.DELIVERY_END) {
            throw new IllegalArgumentException("배송이 완료된 주문만 교환 신청이 가능합니다.");
        }
        this.orderStatus = OrderStatus.EXCHANGE;
    }

    public void checking() {
        if((OrderStatus.EXCHANGE != this.orderStatus) &&
                (OrderStatus.REFUND != this.orderStatus)) {
            throw new IllegalArgumentException("교환/환불 이 요청된 주문 상품만 검수처리 가능합니다.");
        }
        this.orderStatus = OrderStatus.CHECKING;
    }

    public void refundEnd() {
        if(OrderStatus.CHECKING != this.orderStatus) {
            throw new IllegalArgumentException("환불상품이 아직 검수가 완료되지 않아 환불 완료 처리가 불가합니다.");
        }
        this.orderStatus = OrderStatus.REFUND_END;

        // 환불이 완료되면, 상품의 재고수량을 되돌린다.
        increaseQuantity();
    }

    public void exchangeEnd() {
        if(OrderStatus.CHECKING != this.orderStatus) {
            throw new IllegalArgumentException("교환상품이 아직 검수가 완료되지 않아 교환 완료 처리가 불가합니다.");
        }
        this.orderStatus = OrderStatus.EXCHANGE_END;
    }

    public boolean isDeliveryComplete() {
        return OrderStatus.DELIVERY_END == this.orderStatus;
    }

    public void gradeDiscount(int gradeDiscountRate) {
        this.gradeDiscountAmount = this.totalPrice * gradeDiscountRate / 100;
    }

    public void couponDiscount(int couponDiscountRate) {
        this.couponDiscountAmount = this.totalPrice * couponDiscountRate / 100;
    }

    public int calculateRealPayPrice() {
        this.realPayPrice = this.totalPrice - this.gradeDiscountAmount - this.couponDiscountAmount;
        return this.realPayPrice;
    }
}
