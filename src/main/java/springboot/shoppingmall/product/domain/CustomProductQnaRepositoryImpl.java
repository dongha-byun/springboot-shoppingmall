package springboot.shoppingmall.product.domain;

import static springboot.shoppingmall.product.domain.QProductQna.*;
import static springboot.shoppingmall.product.domain.QProductQnaAnswer.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.product.application.dto.ProductQnaDto;
import springboot.shoppingmall.product.dto.QProductQnaAnswerDto;
import springboot.shoppingmall.product.dto.QProductQnaDto;

@RequiredArgsConstructor
@Repository
public class CustomProductQnaRepositoryImpl implements CustomProductQnaRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ProductQnaDto> findAllProductQna(Long productId) {
        return jpaQueryFactory
                .select(new QProductQnaDto(
                        productQna.id,
                        productQna.content,
                        productQna.writeDate,
                        new QProductQnaAnswerDto(
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
