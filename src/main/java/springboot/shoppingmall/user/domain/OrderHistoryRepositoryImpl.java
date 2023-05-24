package springboot.shoppingmall.user.domain;

import static springboot.shoppingmall.order.domain.QOrder.*;
import static springboot.shoppingmall.order.domain.QOrderItem.*;
import static springboot.shoppingmall.pay.domain.QPayHistory.*;
import static springboot.shoppingmall.providers.domain.QProvider.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.order.domain.QOrderItem;
import springboot.shoppingmall.pay.domain.QPayHistory;
import springboot.shoppingmall.providers.domain.QProvider;
import springboot.shoppingmall.user.dto.OrderHistoryDto;

@RequiredArgsConstructor
@Repository
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderHistoryDto> queryOrderHistory(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory.select(
                        Projections.constructor(OrderHistoryDto.class,
                                order.id, order.orderDate, orderItem.orderStatus,
                                orderItem.product.id, orderItem.product.name, payHistory.tid,
                                order.totalPrice, provider.id, provider.name)
                )
                .from(orderItem)
                .join(order).on(orderItem.order.eq(order))
                .join(provider).on(provider.id.eq(orderItem.product.partnerId))
                .join(payHistory).on(payHistory.orderId.eq(order.id))
                .where(
                        order.userId.eq(user.getId())
                                .and(order.orderDate.between(startDate, endDate))
                )
                .orderBy(order.orderDate.desc())
                .fetch();
    }
}
