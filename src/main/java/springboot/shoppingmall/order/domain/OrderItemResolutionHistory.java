package springboot.shoppingmall.order.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item_resolution_history")
@Entity
public class OrderItemResolutionHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    private OrderItemResolutionType resolutionType;
    private LocalDateTime date;
    private String reason;

    public OrderItemResolutionHistory(OrderItem orderItem, OrderItemResolutionType resolutionType, LocalDateTime date, String reason) {
        this.orderItem = orderItem;
        this.resolutionType = resolutionType;
        this.date = date;
        this.reason = reason;
    }
}
