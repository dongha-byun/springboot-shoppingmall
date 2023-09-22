package springboot.shoppingmall.product.domain;

import static springboot.shoppingmall.product.domain.QProductReview.productReview;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.product.application.dto.ProductReviewDto;

@Repository
public class CustomProductReviewRepositoryImpl implements CustomProductReviewRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public CustomProductReviewRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ProductReviewDto> findAllProductReview(Long productId) {
        return jpaQueryFactory.select(Projections.constructor(ProductReviewDto.class,
                                productReview.id,
                                productReview.content,
                                productReview.score,
                                productReview.writeDate
                        )
                ).from(productReview)
                .orderBy(productReview.writeDate.desc())
                .fetch();
    }
}
