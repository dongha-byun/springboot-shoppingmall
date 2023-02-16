package springboot.shoppingmall.product.domain;

import static springboot.shoppingmall.product.domain.QProductReview.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.product.dto.ProductReviewDto;
import springboot.shoppingmall.product.dto.QProductReviewDto;

@Repository
public class CustomProductReviewRepositoryImpl implements CustomProductReviewRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;

    public CustomProductReviewRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<ProductReviewDto> findAllProductReview(Long productId) {
        query
                .select(
                        new QProductReviewDto(
                                productReview.id,
                                productReview.content,
                                productReview.writeDate,
                                null
                        )
                ).from(productReview);

        return em.createQuery("select "
                        + "new springboot.shoppingmall.product.dto.ProductReviewDto(review.id, review.content, review.writeDate, user.userName) "
                        + "from ProductReview review "
                        + "left outer join User user on user.id = review.userId "
                        + "where review.product.id = :productId", ProductReviewDto.class)
                .setParameter("productId", productId)
                .getResultList();
    }
}
