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

    @Column(unique = true)
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime cancelDate;
    private String cancelReason;

    public OrderItem(Product product, int quantity, OrderStatus orderStatus) {
        product.validateQuantity(quantity);

        this.product = product;
        this.quantity = quantity;
        this.orderStatus = orderStatus;

        // 상품 주문 정보 생성 시, 상품의 재고수량을 감소시킨다.
        removeQuantity();
    }

    public static OrderItem createOrderItem(Product product, int quantity) {
        return new OrderItem(product, quantity, OrderStatus.READY);
    }

    public void ordered(Order order){
        this.order = order;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int totalPrice() {
        return product.getPrice() * quantity;
    }

    public void increaseSalesVolume() {
        product.increaseSalesVolume(quantity);
    }

    public void increaseQuantity() {
        product.increaseCount(quantity);
    }

    public void removeQuantity() {
        product.removeCount(quantity);
    }

    public void cancel(LocalDateTime cancelDate, String cancelReason) {
        if(OrderStatus.READY != this.orderStatus) {
            throw new IllegalArgumentException("준비 중인 주문 만 취소가 가능합니다.");
        }
        this.orderStatus = OrderStatus.CANCEL;
        this.cancelDate = cancelDate;
        this.cancelReason = cancelReason;

        // 취소 처리 하면 상품의 재고를 올린다.
        increaseQuantity();
    }

}
