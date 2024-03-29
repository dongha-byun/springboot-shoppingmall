package springboot.shoppingmall.orderhistory.domain;

import static springboot.shoppingmall.order.domain.QOrder.*;
import static springboot.shoppingmall.order.domain.QOrderItem.*;
import static springboot.shoppingmall.partners.domain.QPartner.*;
import static springboot.shoppingmall.pay.domain.QPayHistory.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.orderhistory.application.dto.OrderHistoryDto;

@RequiredArgsConstructor
@Repository
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderHistoryDto> queryOrderHistory(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory.select(
                        Projections.constructor(OrderHistoryDto.class,
                                order.id, orderItem.id, order.orderDate, orderItem.orderStatus,
                                orderItem.product.id, orderItem.product.name, payHistory.tid,
                                order.totalPrice, partner.id, partner.name)
                )
                .from(orderItem)
                .join(order).on(orderItem.order.eq(order))
                .join(partner).on(partner.id.eq(orderItem.product.partnerId))
                .join(payHistory).on(payHistory.orderId.eq(order.id))
                .where(
                        order.userId.eq(userId)
                                .and(order.orderDate.between(startDate, endDate))
                )
                .orderBy(order.orderDate.desc())
                .fetch();
    }
}
