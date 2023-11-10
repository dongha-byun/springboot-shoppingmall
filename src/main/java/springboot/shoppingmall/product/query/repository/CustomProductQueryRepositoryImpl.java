package springboot.shoppingmall.product.query.repository;

import static springboot.shoppingmall.partners.domain.QPartner.*;
import static springboot.shoppingmall.product.domain.QProduct.product;
import static springboot.shoppingmall.product.query.ProductQueryOrderType.*;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;

public class CustomProductQueryRepositoryImpl implements CustomProductQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;
    public CustomProductQueryRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ProductQueryDto> queryProducts(Category category, Category subCategory, ProductQueryOrderType orderType) {
        return jpaQueryFactory.select(
                        projectionsConstructorOfProductQueryDto()
                )
                .from(product)
                .join(partner).on(partner.id.eq(product.partnerId))
                .where(
                        allCategoryEq(category, subCategory)
                )
                .orderBy(
                        orderBy(orderType)
                )
                .fetch();
    }

    @Override
    public List<ProductQueryDto> searchProducts(Category category, Category subCategory, String searchKeyword) {
        return defaultQueryProductsByCategory(category, subCategory)
                .where(
                        searchProductName(searchKeyword)
                ).fetch();
    }

    @Override
    public List<ProductQueryDto> queryProducts(Category category, Category subCategory, ProductQueryOrderType orderType,
                                       int limit, int offset) {
        return defaultQueryProductsByCategory(category, subCategory)
                .orderBy(orderBy(orderType))
                .limit(limit)
                .offset(offset)
                .fetch();
    }

    @Override
    public List<ProductQueryDto> queryPartnersProducts(Long partnerId, Category category, Category subCategory,
                                               int limit, int offset) {
        return jpaQueryFactory.select(
                        projectionsConstructorOfProductQueryDto()
                ).from(product)
                .join(partner).on(partner.id.eq(product.partnerId))
                .where(
                        product.partnerId.eq(partnerId)
                                .and(product.category.eq(category))
                                .and(product.subCategory.eq(subCategory))
                )
                .orderBy(orderBy(RECENT))
                .limit(limit)
                .offset(offset)
                .fetch();
    }

    @Override
    public List<ProductQueryDto> searchProducts(String searchKeyword, ProductQueryOrderType orderType,
                                                int limit, int offset) {
        return jpaQueryFactory.select(
                        projectionsConstructorOfProductQueryDto()
                )
                .from(product)
                .join(partner).on(partner.id.eq(product.partnerId))
                .where(
                        searchProductName(searchKeyword)
                )
                .orderBy(
                        orderBy(orderType)
                )
                .limit(limit)
                .offset(offset)
                .fetch();
    }

    @Override
    public ProductQueryDto findProductOf(Long productId) {
        return jpaQueryFactory.select(
                        projectionsConstructorOfProductQueryDto()
                )
                .from(product)
                .join(partner).on(product.partnerId.eq(partner.id))
                .where(product.id.eq(productId))
                .fetchOne();
    }

    private BooleanExpression searchProductName(String searchText) {
        return product.name.contains(searchText);
    }

    private JPAQuery<ProductQueryDto> defaultQueryProductsByCategory(Category category, Category subCategory) {
        return jpaQueryFactory.select(
                        projectionsConstructorOfProductQueryDto()
                ).from(product)
                .join(partner).on(partner.id.eq(product.partnerId))
                .where(
                        allCategoryEq(category, subCategory)
                );
    }

    private ConstructorExpression<ProductQueryDto> projectionsConstructorOfProductQueryDto() {
        return Projections.constructor(ProductQueryDto.class,
                product.id, product.name,
                product.price, product.quantity,
                product.score, product.salesVolume,
                product.registerDate,
                product.thumbnail.storedFileName,
                product.thumbnail.viewFileName,
                partner.id, partner.name
        );
    }

    private BooleanExpression allCategoryEq(Category category, Category subCategory) {
        return categoryEq(category).and(subCategoryEq(subCategory));
    }

    private BooleanExpression categoryEq(Category category){
        return category == null ? null : product.category.eq(category);
    }

    private BooleanExpression subCategoryEq(Category category){
        return category == null ? null : product.subCategory.eq(category);
    }

    private OrderSpecifier orderBy(ProductQueryOrderType orderType) {
        if(orderType == SCORE) {
            return product.score.desc();
        }

        if(orderType == RECENT) {
            return product.registerDate.desc();
        }

        if(orderType == PRICE) {
            return product.price.asc();
        }

        if(orderType == SELL) {
            return product.salesVolume.desc();
        }

        return null;
    }
}
