package springboot.shoppingmall.product.query.repository;

import static springboot.shoppingmall.product.domain.QProductQna.*;
import static springboot.shoppingmall.product.domain.QProductQnaAnswer.*;
import static springboot.shoppingmall.product.query.ProductQnaAnswerCompleteType.*;
import static springboot.shoppingmall.user.domain.QUser.*;

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
    private final EntityManager em;

    public PartnersProductQnaRepositoryImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
        this.em = entityManager;
    }

    @Override
    public List<PartnersProductQnaDto> findPartnersProductQnaAll(Long partnerId,
                                                                 ProductQnaAnswerCompleteType completeType) {
//        return em.createQuery("select new springboot.shoppingmall.product.query.dto.PartnersProductQnaDto(qna.id, qna.content, u.userName, qna.writeDate, false) "
//                + "from ProductQna qna "
//                + "join User u on qna.writerId=u.id "
//                + "left join ProductQnaAnswer answer on qna.answer=answer "
//                + "where qna.product.partnerId = :partnerId "
//                + "order by qna.writeDate ", PartnersProductQnaDto.class)
//                .setParameter("partnerId", partnerId)
//                .getResultList();

        return jpaQueryFactory.select(Projections.constructor(PartnersProductQnaDto.class,
                        productQna.id, productQna.content,
                        user.userName, productQna.writeDate, productQnaAnswer.id.isNotNull()))
                .from(productQna)
                .join(user).on(user.id.eq(productQna.writerId))
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
