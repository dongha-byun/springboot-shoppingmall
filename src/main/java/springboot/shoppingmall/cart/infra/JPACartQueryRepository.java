package springboot.shoppingmall.cart.infra;

import static springboot.shoppingmall.cart.domain.QCart.*;
import static springboot.shoppingmall.product.domain.QProduct.*;
import static springboot.shoppingmall.partners.domain.QProvider.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.cart.application.dto.CartQueryDto;
import springboot.shoppingmall.cart.domain.CartQueryRepository;

@Repository
public class JPACartQueryRepository implements CartQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public JPACartQueryRepository(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CartQueryDto> findAllCartByUserId(Long userId) {
        return jpaQueryFactory
                .select(Projections.constructor(CartQueryDto.class,
                        cart.id, cart.quantity, product.id,
                        product.name, product.price,
                        provider.id, provider.name,
                        product.thumbnail.storedFileName
                        ))
                .from(cart)
                .join(product).on(product.id.eq(cart.productId))
                .join(provider).on(provider.id.eq(product.partnerId))
                .where(cart.userId.eq(userId))
                .fetch();
    }
}
