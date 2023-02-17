package springboot.shoppingmall.product.domain;

import static springboot.shoppingmall.product.domain.QProductQna.*;
import static springboot.shoppingmall.user.domain.QUser.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.product.dto.ProductQnaDto;
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
                        user.userName
                ))
                .from(productQna)
                .leftJoin(user).on(user.id.eq(productQna.writerId))
                .where(productQna.product.id.eq(productId))
                .orderBy(productQna.writeDate.desc())
                .fetch();
    }
}
