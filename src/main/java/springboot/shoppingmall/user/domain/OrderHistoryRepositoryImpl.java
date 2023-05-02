package springboot.shoppingmall.user.domain;

import static springboot.shoppingmall.order.domain.QOrder.*;
import static springboot.shoppingmall.providers.domain.QProvider.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
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
                                order.id, order.orderDate, order.orderStatus,
                                order.product.name, order.totalPrice, provider.id, provider.name)
                )
                .from(order)
                .join(provider).on(provider.id.eq(order.product.partnerId))
                .where(
                        order.userId.eq(user.getId())
                                .and(order.orderDate.between(startDate, endDate))
                )
                .orderBy(order.orderDate.desc())
                .fetch();
    }
}
