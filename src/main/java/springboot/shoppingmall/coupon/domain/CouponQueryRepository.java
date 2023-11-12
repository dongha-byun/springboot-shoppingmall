package springboot.shoppingmall.coupon.domain;

import static springboot.shoppingmall.coupon.domain.QCoupon.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.coupon.application.CouponQueryDto;

@Repository
public class CouponQueryRepository {
    private final JPAQueryFactory queryFactory;

    public CouponQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<CouponQueryDto> findCouponAll(Long partnerId) {
        return queryFactory.select(
                        Projections.constructor(CouponQueryDto.class,
                                coupon.id, coupon.name,
                                coupon.usingDuration.fromDate,
                                coupon.usingDuration.toDate,
                                coupon.discountRate
                                )
                )
                .from(coupon)
                .where(
                        coupon.partnerId.eq(partnerId)
                )
                .orderBy(
                        coupon.usingDuration.toDate.asc(),
                        coupon.usingDuration.fromDate.asc(),
                        coupon.id.desc()
                ).fetch();
    }
}
