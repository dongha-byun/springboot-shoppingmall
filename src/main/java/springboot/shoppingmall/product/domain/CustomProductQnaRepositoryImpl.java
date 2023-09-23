package springboot.shoppingmall.product.domain;

import static springboot.shoppingmall.product.domain.QProductQna.*;
import static springboot.shoppingmall.product.domain.QProductQnaAnswer.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.product.application.dto.ProductQnaAnswerDto;
import springboot.shoppingmall.product.application.dto.ProductQnaDto;

@RequiredArgsConstructor
@Repository
public class CustomProductQnaRepositoryImpl implements CustomProductQnaRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ProductQnaDto> findAllProductQna(Long productId) {
        return jpaQueryFactory
                .select(Projections.constructor(ProductQnaDto.class,
                        productQna.id,
                        productQna.content,
                        productQna.writeDate,
                        Projections.constructor(ProductQnaAnswerDto.class,
                                productQnaAnswer.id,
                                productQnaAnswer.answer,
                                productQnaAnswer.answerDate
                        )
                ))
                .from(productQna)
                .leftJoin(productQnaAnswer).on(productQnaAnswer.productQna.eq(productQna))
                .where(productQna.product.id.eq(productId))
                .orderBy(productQna.writeDate.desc())
                .fetch();
    }
}
