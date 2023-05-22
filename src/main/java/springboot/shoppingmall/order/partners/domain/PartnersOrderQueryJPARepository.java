package springboot.shoppingmall.order.partners.domain;

import static springboot.shoppingmall.order.domain.QOrder.order;
import static springboot.shoppingmall.order.domain.QOrderItem.*;
import static springboot.shoppingmall.user.domain.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.dto.PartnersCancelOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersDeliveryOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersEndOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersReadyOrderQueryDto;

public class PartnersOrderQueryJPARepository implements PartnersOrderQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public PartnersOrderQueryJPARepository(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PartnersReadyOrderQueryDto> findPartnersReadyOrders(Long partnerId,
                                                                    LocalDateTime startDate,
                                                                    LocalDateTime endDate) {
        return jpaQueryFactory.select(
                        Projections.constructor(PartnersReadyOrderQueryDto.class,
                                orderItem.id, orderItem.order.orderCode, orderItem.order.orderDate,
                                orderItem.product.productCode, orderItem.product.name,
                                orderItem.quantity, orderItem.invoiceNumber,
                                orderItem.order.totalPrice, user.userName, user.telNo.telNo,
                                orderItem.order.orderStatus, orderItem.order.receiverName,
                                orderItem.order.address, orderItem.order.detailAddress,
                                orderItem.order.requestMessage))
                .from(orderItem)
                .join(user).on(user.id.eq(orderItem.order.userId))
                .where(
                        equalPartners(partnerId)
                                .and(inOrderStatus(PartnersOrderQueryType.READY.getStatusList()))
                                .and(betweenDate(startDate, endDate))
                ).fetch();
    }

    @Override
    public List<PartnersDeliveryOrderQueryDto> findPartnersDeliveryOrders(Long partnerId,
                                                                          LocalDateTime startDate,
                                                                          LocalDateTime endDate) {
        return jpaQueryFactory.select(
                        Projections.constructor(PartnersDeliveryOrderQueryDto.class,
                                orderItem.id, orderItem.order.orderCode, orderItem.order.orderDate,
                                orderItem.product.productCode, orderItem.product.name,
                                orderItem.quantity, orderItem.invoiceNumber,
                                orderItem.order.totalPrice, user.userName, user.telNo.telNo,
                                orderItem.order.orderStatus, orderItem.order.receiverName,
                                orderItem.order.address, orderItem.order.detailAddress,
                                orderItem.order.requestMessage))
                .from(orderItem)
                .join(user).on(user.id.eq(orderItem.order.userId))
                .where(
                        equalPartners(partnerId)
                                .and(inOrderStatus(PartnersOrderQueryType.DELIVERY.getStatusList()))
                                .and(betweenDate(startDate, endDate))
                ).fetch();
    }

    @Override
    public List<PartnersEndOrderQueryDto> findPartnersEndOrders(Long partnerId,
                                                                LocalDateTime startDate,
                                                                LocalDateTime endDate) {
        return jpaQueryFactory.select(
                        Projections.constructor(PartnersEndOrderQueryDto.class,
                                orderItem.id, orderItem.order.orderCode, orderItem.order.orderDate,
                                orderItem.product.productCode, orderItem.product.name,
                                orderItem.quantity, orderItem.invoiceNumber,
                                orderItem.order.totalPrice, user.userName, user.telNo.telNo,
                                orderItem.order.orderStatus, orderItem.order.receiverName,
                                orderItem.order.address, orderItem.order.detailAddress,
                                orderItem.order.requestMessage, orderItem.order.invoiceNumber,
                                orderItem.order.deliveryDate, orderItem.order.deliveryPlace))
                .from(orderItem)
                .join(user).on(user.id.eq(orderItem.order.userId))
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
                                orderItem.id, orderItem.order.orderCode, orderItem.order.orderDate,
                                orderItem.product.productCode, orderItem.product.name,
                                orderItem.quantity, orderItem.invoiceNumber,
                                orderItem.order.totalPrice, user.userName, user.telNo.telNo,
                                orderItem.order.orderStatus,
                                orderItem.order.cancelDate, orderItem.order.cancelReason,
                                orderItem.order.refundDate, orderItem.order.refundReason,
                                orderItem.order.exchangeDate, orderItem.order.exchangeReason))
                .from(order)
                .join(user).on(user.id.eq(order.userId))
                .where(
                        equalPartners(partnerId)
                                .and(inOrderStatus(PartnersOrderQueryType.CANCEL.getStatusList()))
                                .and(betweenDate(startDate, endDate))
                ).fetch();
    }

    private BooleanExpression betweenDate(LocalDateTime startDate, LocalDateTime endDate) {
        return orderItem.order.orderDate.between(startDate, endDate);
    }

    private BooleanExpression inOrderStatus(List<OrderStatus> status) {
        return orderItem.order.orderStatus.in(status);
    }

    private BooleanExpression equalPartners(Long partnerId) {
        return orderItem.product.partnerId.eq(partnerId);
    }
}
