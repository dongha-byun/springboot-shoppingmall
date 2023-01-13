package springboot.shoppingmall.user.domain;

import static springboot.shoppingmall.order.domain.QOrder.*;
import static springboot.shoppingmall.user.domain.QUser.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.user.dto.OrderHistoryDto;

@RequiredArgsConstructor
@Repository
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OrderHistoryDto> queryOrderHistory(User user) {
        return jpaQueryFactory.select(
                        Projections.constructor(OrderHistoryDto.class,
                                order.id, order.orderDate,
                                order.orderStatus, order.product.name, order.totalPrice)
                )
                .from(order)
                .where(
                        order.user.eq(user)
                )
                .fetch();
    }
}
