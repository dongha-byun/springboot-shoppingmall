package springboot.shoppingmall.order.partners.domain;

import static springboot.shoppingmall.order.domain.QOrder.order;
import static springboot.shoppingmall.product.domain.QProduct.product;
import static springboot.shoppingmall.user.domain.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;

public class PartnersOrderQueryJPARepository implements PartnersOrderQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public PartnersOrderQueryJPARepository(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PartnersOrderQueryDto> findPartnersOrders(Long partnerId, OrderStatus status, LocalDateTime startDate,
                                                          LocalDateTime endDate) {
        return jpaQueryFactory.select(Projections.constructor(PartnersOrderQueryDto.class,
                        order.id, order.orderCode, order.orderDate, product.productCode, product.name, order.quantity,
                        user.userName, user.telNo.telNo, order.receiverName, order.address, order.detailAddress,
                        order.requestMessage))
                .from(order)
                .join(product).on(product.id.eq(order.product.id))
                .join(user).on(user.id.eq(order.userId))
                .where(
                        product.partnerId.eq(partnerId)
                                .and(order.orderStatus.eq(status))
                                .and(order.orderDate.between(startDate, endDate))
                ).fetch();
    }
}
