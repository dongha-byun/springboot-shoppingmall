package springboot.shoppingmall.coupon.domain;

import static springboot.shoppingmall.coupon.domain.QCoupon.*;
import static springboot.shoppingmall.coupon.domain.QUserCoupon.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.coupon.application.UsableCouponDto;

@Repository
public class UserCouponQueryRepository {
    private final JPAQueryFactory queryFactory;

    public UserCouponQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<UsableCouponDto> findUsableCouponList(Long userId, Long partnersId) {
        return queryFactory.select(
                Projections.constructor(
                        UsableCouponDto.class,
                        userCoupon.id,
                        coupon.name,
                        coupon.usingDuration.fromDate,
                        coupon.usingDuration.toDate,
                        coupon.discountRate
                        )
                )
                .from(coupon)
                .join(coupon.userCoupons, userCoupon)
                .where(
                        userCoupon.userId.eq(userId)
                                .and(
                                        coupon.partnersId.eq(partnersId)
                                )
                                .and(
                                        userCoupon.usingDate.isNull()
                                )
                ).orderBy(
                        coupon.discountRate.desc()
                ).stream().collect(Collectors.toList());
    }

    public List<UserCouponDto> getUserIdsHasCoupon(Long couponId) {
        return queryFactory.select(
                        Projections.constructor(UserCouponDto.class,
                                userCoupon.userId,
                                userCoupon.usingDate
                                )
                )
                .from(userCoupon)
                .where(
                        userCoupon.coupon.id.eq(couponId)
                ).stream().collect(Collectors.toList());
    }
}
