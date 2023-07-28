package springboot.shoppingmall.coupon.domain;

import static springboot.shoppingmall.coupon.domain.QCoupon.*;
import static springboot.shoppingmall.coupon.domain.QUserCoupon.*;
import static springboot.shoppingmall.user.domain.QUser.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserCouponQueryRepository {
    private final JPAQueryFactory queryFactory;

    public UserCouponQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<UserCouponQueryDto> findAllUserReceivedCoupon(Coupon coupon) {
        return queryFactory.select(
                        Projections.constructor(UserCouponQueryDto.class,
                                user.id, user.userName,
                                user.userGradeInfo.grade, userCoupon.usingDate
                        )
                ).from(userCoupon)
                .join(user).on(user.id.eq(userCoupon.userId))
                .where(
                        userCoupon.coupon.eq(coupon)
                ).orderBy(
                        user.userName.asc()
                ).stream().collect(Collectors.toList());
    }

    public List<Coupon> findUsableCouponList(Long userId, Long partnersId) {
        return queryFactory.selectFrom(coupon)
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
}
