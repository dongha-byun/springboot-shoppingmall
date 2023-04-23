package springboot.shoppingmall.cart.domain;

import static springboot.shoppingmall.cart.domain.QCart.*;
import static springboot.shoppingmall.product.domain.QProduct.*;
import static springboot.shoppingmall.providers.domain.QProvider.*;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.cart.dto.CartDto;

@Repository
public class CartQueryRepositoryImpl implements CartQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public CartQueryRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CartDto> findAllCartByUserId(Long userId) {
        return jpaQueryFactory
                .select(Projections.constructor(CartDto.class,
                        cart.id, cart.quantity, product.id,
                        product.name, product.price, provider.name,
                        product.thumbnail.storedFileName
                        ))
                .from(cart)
                .join(product).on(product.id.eq(cart.product.id))
                .join(provider).on(provider.id.eq(product.partnerId))
                .where(cart.user.id.eq(userId))
                .fetch();
    }
}
