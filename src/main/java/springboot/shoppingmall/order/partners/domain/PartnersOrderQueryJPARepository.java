package springboot.shoppingmall.order.partners.domain;

import static springboot.shoppingmall.order.domain.QOrder.order;
import static springboot.shoppingmall.product.domain.QProduct.product;
import static springboot.shoppingmall.user.domain.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.controller.PartnersOrderQueryResponse;
import springboot.shoppingmall.order.partners.controller.PartnersReadyOrderQueryResponse;
import springboot.shoppingmall.order.partners.dto.PartnersCancelOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersReadyOrderQueryDto;

public class PartnersOrderQueryJPARepository implements PartnersOrderQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public PartnersOrderQueryJPARepository(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PartnersReadyOrderQueryDto> findPartnersReadyOrders(Long partnerId,
                                                                    LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory.select(
                        Projections.constructor(PartnersReadyOrderQueryDto.class,
                                order.id, order.orderCode, order.orderDate, product.productCode, product.name,
                                order.quantity, order.totalPrice, user.userName, user.telNo.telNo,
                                order.orderStatus, order.receiverName, order.address, order.detailAddress,
                                order.requestMessage, order.invoiceNumber))
                .from(order)
                .join(product).on(product.id.eq(order.product.id))
                .join(user).on(user.id.eq(order.userId))
                .where(
                        equalPartners(partnerId)
                                .and(inOrderStatus(PartnersOrderQueryType.READY.getStatusList()))
                                .and(betweenDate(startDate, endDate))
                ).fetch();
    }

    @Override
    public List<PartnersDeliveryOrderQueryDto> findPartnersDeliveryOrders(Long partnerId, LocalDateTime startDate,
                                                                          LocalDateTime endDate) {
        return jpaQueryFactory.select(
                        Projections.constructor(PartnersDeliveryOrderQueryDto.class,
                                order.id, order.orderCode, order.orderDate, product.productCode, product.name,
                                order.quantity, order.totalPrice, user.userName, user.telNo.telNo,
                                order.orderStatus, order.receiverName, order.address, order.detailAddress,
                                order.requestMessage, order.invoiceNumber))
                .from(order)
                .join(product).on(product.id.eq(order.product.id))
                .join(user).on(user.id.eq(order.userId))
                .where(
                        equalPartners(partnerId)
                                .and(inOrderStatus(PartnersOrderQueryType.DELIVERY.getStatusList()))
                                .and(betweenDate(startDate, endDate))
                ).fetch();
    }

    @Override
    public List<PartnersEndOrderQueryDto> findPartnersEndOrders(Long partnerId, LocalDateTime startDate,
                                                                LocalDateTime endDate) {
        return jpaQueryFactory.select(
                        Projections.constructor(PartnersEndOrderQueryDto.class,
                                order.id, order.orderCode, order.orderDate,
                                product.productCode, product.name, order.quantity, order.totalPrice,
                                user.userName, user.telNo.telNo, order.orderStatus,
                                order.receiverName, order.address, order.detailAddress,
                                order.requestMessage, order.invoiceNumber,
                                order.deliveryDate, order.deliveryPlace))
                .from(order)
                .join(product).on(product.id.eq(order.product.id))
                .join(user).on(user.id.eq(order.userId))
                .where(
                        equalPartners(partnerId)
                                .and(inOrderStatus(PartnersOrderQueryType.END.getStatusList()))
                                .and(betweenDate(startDate, endDate))
                ).fetch();
    }

    @Override
    public List<PartnersCancelOrderQueryDto> findPartnersCancelOrders(Long partnerId, LocalDateTime startDate,
                                                                      LocalDateTime endDate) {
        return jpaQueryFactory.select(
                        Projections.constructor(PartnersCancelOrderQueryDto.class,
                                order.id, order.orderCode, order.orderDate,
                                product.productCode, product.name, order.quantity, order.totalPrice,
                                user.userName, user.telNo.telNo, order.orderStatus,
                                order.cancelDate, order.cancelReason,
                                order.refundDate, order.refundReason,
                                order.exchangeDate, order.exchangeReason))
                .from(order)
                .join(product).on(product.id.eq(order.product.id))
                .join(user).on(user.id.eq(order.userId))
                .where(
                        equalPartners(partnerId)
                                .and(inOrderStatus(PartnersOrderQueryType.CANCEL.getStatusList()))
                                .and(betweenDate(startDate, endDate))
                ).fetch();
    }

    private BooleanExpression betweenDate(LocalDateTime startDate, LocalDateTime endDate) {
        return order.orderDate.between(startDate, endDate);
    }

    private BooleanExpression inOrderStatus(List<OrderStatus> status) {
        return order.orderStatus.in(status);
    }

    private BooleanExpression equalPartners(Long partnerId) {
        return product.partnerId.eq(partnerId);
    }
}
