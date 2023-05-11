package springboot.shoppingmall.order.partners.domain;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import springboot.shoppingmall.order.domain.OrderStatus;

@Getter
public enum PartnersOrderQueryType {
    READY("준비중", Arrays.asList(OrderStatus.READY, OrderStatus.OUTING)),
    DELIVERY("배송중", List.of(OrderStatus.DELIVERY)),
    END("배송완료", List.of(OrderStatus.DELIVERY_END)),
    CANCEL("취소/환불/교환", Arrays.asList(OrderStatus.CANCEL, OrderStatus.REFUND, OrderStatus.REFUND_END, OrderStatus.EXCHANGE, OrderStatus.CHECKING));

    private final String name;
    private final List<OrderStatus> statusList;

    PartnersOrderQueryType(String name, List<OrderStatus> statusList) {
        this.name = name;
        this.statusList = statusList;
    }
}
