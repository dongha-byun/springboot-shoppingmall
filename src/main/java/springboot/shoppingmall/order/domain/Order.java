package springboot.shoppingmall.order.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import springboot.shoppingmall.BaseEntity;
import springboot.shoppingmall.product.domain.Product;

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

    @Column(unique = true)
    private String invoiceNumber;

    private LocalDateTime orderDate;
    private int totalPrice;
    private String receiverName;
    private String zipCode;
    private String address;
    private String detailAddress;
    private String requestMessage;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDateTime cancelDate;
    private String cancelReason;
    private LocalDateTime refundDate;
    private String refundReason;
    private LocalDateTime exchangeDate;
    private String exchangeReason;
    private LocalDateTime deliveryDate;
    private String deliveryPlace;

    public Order(String orderCode, Long userId, List<OrderItem> items, OrderStatus orderStatus,
                 String receiverName, String zipCode, String address, String detailAddress,
                 String requestMessage){
        this(orderCode, userId, items, LocalDateTime.now(), orderStatus,
                receiverName, zipCode, address, detailAddress, requestMessage);
    }

    public Order(String orderCode, Long userId, List<OrderItem> items, LocalDateTime orderDate,
                 OrderStatus orderStatus, String receiverName, String zipCode, String address,
                 String detailAddress, String requestMessage) {
        this.orderCode = orderCode;
        this.userId = userId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.receiverName = receiverName;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.requestMessage = requestMessage;

        initItems(items);
        calculateTotalPrice();
    }

    private void initItems(List<OrderItem> items) {
        items.forEach(this::addOrderItem);
    }

    public static Order createOrder(String orderCode, Long userId, List<OrderItem> items, String receiverName,
                                    String zipCode, String address, String detailAddress, String requestMessage){
        return new Order(orderCode, userId, items, OrderStatus.READY,
                receiverName, zipCode, address, detailAddress, requestMessage);
    }

    public void cancel(LocalDateTime cancelDate, String cancelReason) {
        if(this.orderStatus != OrderStatus.READY){
            throw new IllegalArgumentException("준비 중인 주문만 취소 가능합니다.");
        }
        this.orderStatus = OrderStatus.CANCEL;
        this.cancelDate = cancelDate;
        this.cancelReason = cancelReason;
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

    public void deliveryEnd(LocalDateTime deliveryDate, String deliveryPlace) {
        if(this.orderStatus != OrderStatus.DELIVERY) {
            throw new IllegalArgumentException("배송 준비중인 주문만 배송완료 처리가 가능합니다.");
        }
        this.orderStatus = OrderStatus.DELIVERY_END;
        this.deliveryDate = deliveryDate;
        this.deliveryPlace = deliveryPlace;
    }

    public void finish() {
        if(this.orderStatus != OrderStatus.DELIVERY_END) {
            throw new IllegalArgumentException("배송이 완료된 주문만 구매확정 처리가 가능합니다.");
        }
        this.orderStatus = OrderStatus.FINISH;
    }

    public void refund(LocalDateTime refundDate, String refundReason) {
        if(!StringUtils.hasText(refundReason)) {
            throw new IllegalArgumentException("환불 사유는 필수입니다.");
        }

        if(this.orderStatus != OrderStatus.DELIVERY_END) {
            throw new IllegalArgumentException("배송이 완료된 주문만 환불 신청이 가능합니다.");
        }

        this.orderStatus = OrderStatus.REFUND;
        this.refundDate = refundDate;
        this.refundReason = refundReason;
    }

    public void exchange(LocalDateTime exchangeDate, String exchangeReason) {
        if(!StringUtils.hasText(exchangeReason)) {
            throw new IllegalArgumentException("교환 사유는 필수입니다.");
        }

        if(this.orderStatus != OrderStatus.DELIVERY_END) {
            throw new IllegalArgumentException("배송이 완료된 주문만 교환 신청이 가능합니다.");
        }
        this.orderStatus = OrderStatus.EXCHANGE;
        this.exchangeDate = exchangeDate;
        this.exchangeReason = exchangeReason;
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

    public void addOrderItem(OrderItem item) {
        this.items.add(item);
        item.ordered(this);
    }

    public void calculateTotalPrice() {
        this.items.forEach(
                orderItem -> this.totalPrice += orderItem.totalPrice()
        );
    }

    public void increaseSalesVolume() {
        items.forEach(
                OrderItem::increaseSalesVolume
        );
    }

    public void increaseQuantity() {
        items.forEach(
                OrderItem::increaseQuantity
        );
    }

    public void removeQuantity() {
        items.forEach(
                OrderItem::removeQuantity
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
}
