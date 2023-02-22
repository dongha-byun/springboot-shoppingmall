package springboot.shoppingmall.order.domain;

import java.math.BigDecimal;
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
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime orderDate;

    private int quantity;

    @Column(unique = true)
    private String invoiceNumber;

    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String refundReason;
    private String exchangeReason;

    public Order(Long userId, Product product, int quantity, Delivery delivery, OrderStatus orderStatus){
        this(userId, product, quantity, delivery, orderStatus, null);
    }

    public Order(Long userId, Product product, int quantity, Delivery delivery, OrderStatus orderStatus, String invoiceNumber) {
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.orderDate = LocalDateTime.now();
        this.orderStatus = orderStatus;
        this.totalPrice = product.getPrice() * quantity;
        this.delivery = delivery;
        this.invoiceNumber = invoiceNumber;
    }

    public static Order createOrder(Long userId, Product product, int quantity, Delivery delivery){
        Order order = new Order();
        order.userId = userId;
        order.product = product;
        order.quantity = quantity;
        order.orderDate = LocalDateTime.now();
        order.orderStatus = OrderStatus.READY;
        order.totalPrice = product.getPrice() * quantity;
        order.delivery = delivery;

        return order;
    }

    public void cancel() {
        if(this.orderStatus != OrderStatus.READY){
            throw new IllegalArgumentException("준비 중인 주문만 취소 가능합니다.");
        }
        this.orderStatus = OrderStatus.CANCEL;
    }

    public void outing() {
        if(this.orderStatus != OrderStatus.READY){
            throw new IllegalArgumentException("준비 중인 주문만 출고중으로 처리 가능합니다.");
        }
        this.orderStatus = OrderStatus.OUTING;
    }

    public void delivery() {
        if(this.orderStatus != OrderStatus.OUTING){
            throw new IllegalArgumentException("출고 중인 주문만 배송중으로 처리 가능합니다.");
        }
        this.orderStatus = OrderStatus.DELIVERY;
    }

    public void deliveryEnd() {
        if(this.orderStatus != OrderStatus.DELIVERY) {
            throw new IllegalArgumentException("배송 준비중인 주문만 배송완료 처리가 가능합니다.");
        }
        this.orderStatus = OrderStatus.DELIVERY_END;
    }

    public void finish() {
        if(this.orderStatus != OrderStatus.DELIVERY_END) {
            throw new IllegalArgumentException("배송이 완료된 주문만 구매확정 처리가 가능합니다.");
        }
        this.orderStatus = OrderStatus.FINISH;
    }

    public void refund(String refundReason) {
        if(!StringUtils.hasText(refundReason)) {
            throw new IllegalArgumentException("환불 사유는 필수입니다.");
        }

        if(this.orderStatus != OrderStatus.DELIVERY_END) {
            throw new IllegalArgumentException("배송이 완료된 주문만 환불 신청이 가능합니다.");
        }

        this.orderStatus = OrderStatus.REFUND;
        this.refundReason = refundReason;
    }

    public void exchange(String exchangeReason) {
        if(!StringUtils.hasText(exchangeReason)) {
            throw new IllegalArgumentException("교환 사유는 필수입니다.");
        }

        if(this.orderStatus != OrderStatus.DELIVERY_END) {
            throw new IllegalArgumentException("배송이 완료된 주문만 교환 신청이 가능합니다.");
        }
        this.orderStatus = OrderStatus.EXCHANGE;
        this.exchangeReason = exchangeReason;
    }

    public void changeStatus(OrderStatus orderStatus) {
        if(OrderStatus.OUTING == orderStatus){
            outing();
        }
        if(OrderStatus.CANCEL == orderStatus){
            cancel();
        }
        if(OrderStatus.DELIVERY == orderStatus){
            delivery();
        }
        if(OrderStatus.DELIVERY_END == orderStatus){
            deliveryEnd();
        }
        if(OrderStatus.FINISH == orderStatus){
            finish();
        }
    }

    public boolean isOuting() {
        return OrderStatus.OUTING.equals(orderStatus);
    }

    public void changeInvoiceNumber(String invoiceNumber) {
        if(StringUtils.hasText(this.invoiceNumber)){
            throw new IllegalArgumentException("기존에 발급받은 송장번호가 존재합니다.");
        }
        this.invoiceNumber = invoiceNumber;
    }

    public boolean isDeliveryEnd() {
        return OrderStatus.DELIVERY_END == this.orderStatus;
    }

    public void refundEnd() {
        this.orderStatus = OrderStatus.REFUND_END;
    }

    public void checking() {
        this.orderStatus = OrderStatus.CHECKING;
    }
}
