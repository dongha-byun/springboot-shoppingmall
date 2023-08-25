package springboot.shoppingmall.product.query.repository;

import static springboot.shoppingmall.product.domain.QProductQna.*;
import static springboot.shoppingmall.product.domain.QProductQnaAnswer.*;
import static springboot.shoppingmall.product.query.ProductQnaAnswerCompleteType.*;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.product.query.ProductQnaAnswerCompleteType;
import springboot.shoppingmall.product.query.dto.PartnersProductQnaDto;

@Repository
public class PartnersProductQnaRepositoryImpl implements PartnersProductQnaRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public PartnersProductQnaRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PartnersProductQnaDto> findPartnersProductQnaAll(Long partnerId,
                                                                 ProductQnaAnswerCompleteType completeType) {

        return jpaQueryFactory.select(Projections.constructor(PartnersProductQnaDto.class,
                        productQna.id, productQna.content,
                        productQna.product.id, productQna.product.name, productQna.product.thumbnail.storedFileName,
                        productQna.writeDate, productQnaAnswer.id.isNotNull()))
                .from(productQna)
                .leftJoin(productQnaAnswer).on(productQnaAnswer.productQna.eq(productQna))
                .where(
                        equalPartner(partnerId)
                                .and(isAnswerCheck(completeType))
                )
                .orderBy(
                        productQna.writeDate.asc()
                )
                .fetch();
    }

    private BooleanExpression equalPartner(Long partnerId) {
        return productQna.product.partnerId.eq(partnerId);
    }

    private BooleanExpression isAnswerCheck(ProductQnaAnswerCompleteType completeType) {
        if(completeType == N) {
            return productQnaAnswer.id.isNull();
        }

        if(completeType == Y) {
            return productQnaAnswer.id.isNotNull();
        }

        return null;
    }
}
