package springboot.shoppingmall.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItem> items = new ArrayList<>();

    private LocalDateTime orderDate;
    private int totalPrice;
    private int realPayPrice;
    @Embedded
    private OrderDeliveryInfo orderDeliveryInfo;

    public static Order createOrder(String orderCode, Long userId, List<OrderItem> items,
                                    OrderDeliveryInfo orderDeliveryInfo){
        return new Order(orderCode, userId, items, orderDeliveryInfo);
    }

    public Order(String orderCode, Long userId, List<OrderItem> items, OrderDeliveryInfo orderDeliveryInfo){
        this(orderCode, userId, items, LocalDateTime.now(), orderDeliveryInfo);
    }

    public Order(String orderCode, Long userId, List<OrderItem> items, LocalDateTime orderDate,
                 OrderDeliveryInfo orderDeliveryInfo) {
        this.orderCode = orderCode;
        this.userId = userId;
        this.orderDate = orderDate;
        this.orderDeliveryInfo = orderDeliveryInfo;

        initItems(items);
    }

    private void initItems(List<OrderItem> items) {
        items.forEach(this::addOrderItem);
        this.totalPrice = calculateTotalPrice();
    }

    private void addOrderItem(OrderItem item) {
        this.items.add(item);
        item.ordered(this);
    }

    private int calculateTotalPrice() {
        return this.items.stream()
                .mapToInt(OrderItem::totalPrice)
                .sum();
    }

    public OrderItem findOrderItem(Long orderItemId) {
        return this.items.stream()
                .filter(orderItem -> orderItem.getId().equals(orderItemId))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException("주문 상품 정보가 존재하지 않습니다.")
                );
    }

    public void gradeDiscount(int gradeDiscountRate) {
        this.items.forEach(item -> item.gradeDiscount(gradeDiscountRate));
    }

    public void calculateRealPayPrice() {
        this.realPayPrice = this.getItems().stream()
                .mapToInt(OrderItem::calculateRealPayPrice)
                .sum();
    }
}
