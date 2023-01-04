package springboot.shoppingmall.order.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime orderDate;

    private int quantity;

    private String orderNo;

    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public static Order createOrder(User user, Product product, int quantity, Delivery delivery){
        Order order = new Order();
        order.user = user;
        order.product = product;
        order.quantity = quantity;
        order.orderDate = LocalDateTime.now();
        order.orderStatus = OrderStatus.READY;
        order.totalPrice = product.getPrice() * quantity;
        order.delivery = delivery;

        return order;
    }

}
