package springboot.shoppingmall.product.domain;

import static springboot.shoppingmall.product.domain.QProductReview.productReview;
import static springboot.shoppingmall.user.domain.QUser.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.product.dto.ProductReviewDto;
import springboot.shoppingmall.product.dto.QProductReviewDto;
import springboot.shoppingmall.user.domain.QUser;

@RequiredArgsConstructor
@Repository
public class CustomProductReviewRepositoryImpl implements CustomProductReviewRepository{

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ProductReviewDto> findAllProductReview(Long productId) {
        return jpaQueryFactory.select(
                        new QProductReviewDto(
                                productReview.id,
                                productReview.content,
                                productReview.writeDate,
                                user.userName
                        )
                ).from(productReview)
                .leftJoin(user).on(user.id.eq(productReview.userId))
                .where(productReview.product.id.eq(productId))
                .orderBy(productReview.writeDate.desc())
                .fetch();
    }
}
