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
    @Embedded
    private OrderDeliveryInfo orderDeliveryInfo;

    public Order(String orderCode, Long userId, List<OrderItem> items,
                 String receiverName, String receiverPhoneNumber,
                 String zipCode, String address, String detailAddress,
                 String requestMessage){
        this(orderCode, userId, items, LocalDateTime.now(),
                receiverName, receiverPhoneNumber,
                zipCode, address, detailAddress, requestMessage);
    }

    public Order(String orderCode, Long userId, List<OrderItem> items, LocalDateTime orderDate,
                 String receiverName, String receiverPhoneNumber,
                 String zipCode, String address,
                 String detailAddress, String requestMessage) {
        this.orderCode = orderCode;
        this.userId = userId;
        this.orderDate = orderDate;
        this.orderDeliveryInfo = new OrderDeliveryInfo(
                receiverName, receiverPhoneNumber,
                zipCode, address, detailAddress, requestMessage
        );

        initItems(items);
        calculateTotalPrice();
    }

    private void initItems(List<OrderItem> items) {
        items.forEach(this::addOrderItem);
    }

    public static Order createOrder(String orderCode, Long userId, List<OrderItem> items,
                                    String receiverName, String receiverPhoneNumber,
                                    String zipCode, String address, String detailAddress, String requestMessage){
        return new Order(orderCode, userId, items,
                receiverName, receiverPhoneNumber,
                zipCode, address, detailAddress, requestMessage);
    }

    public void addOrderItem(OrderItem item) {
        this.items.add(item);
        item.ordered(this);
    }

    public void calculateTotalPrice() {
        this.items.forEach(
                orderItem -> this.totalPrice += orderItem.totalPrice()
        );
    }

    public OrderItem findOrderItem(Long orderItemId) {
        return this.items.stream()
                .filter(orderItem -> orderItem.getId().equals(orderItemId))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException("주문 상품 정보가 존재하지 않습니다.")
                );
    }

    public String getReceiverName() {
        return this.orderDeliveryInfo.getReceiverName();
    }
    public String getReceiverPhoneNumber() {
        return this.orderDeliveryInfo.getReceiverPhoneNumber();
    }
    public String getZipCode() {
        return this.orderDeliveryInfo.getZipCode();
    }

    public String getAddress() {
        return this.orderDeliveryInfo.getAddress();
    }

    public String getDetailAddress() {
        return this.orderDeliveryInfo.getDetailAddress();
    }

    public String getRequestMessage() {
        return this.orderDeliveryInfo.getRequestMessage();
    }
}
