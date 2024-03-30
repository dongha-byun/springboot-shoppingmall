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
    public List<ProductQueryDto> queryProducts(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType) {
        return jpaQueryFactory.select(
                        projectionsConstructorOfProductQueryDto()
                )
                .from(product)
                .join(partner).on(partner.id.eq(product.partnerId))
                .where(
                        allCategoryEq(categoryId, subCategoryId)
                )
                .orderBy(
                        orderBy(orderType)
                )
                .fetch();
    }

    @Override
    public List<ProductQueryDto> searchProducts(Long categoryId, Long subCategoryId, String searchKeyword) {
        return defaultQueryProductsByCategory(categoryId, subCategoryId)
                .where(
                        searchProductName(searchKeyword)
                ).fetch();
    }

    @Override
    public List<ProductQueryDto> queryProducts(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType,
                                       int limit, int offset) {
        return defaultQueryProductsByCategory(categoryId, subCategoryId)
                .orderBy(orderBy(orderType))
                .limit(limit)
                .offset(offset)
                .fetch();
    }

    @Override
    public List<ProductQueryDto> queryPartnersProducts(Long partnerId, Long categoryId, Long subCategoryId,
                                               int limit, int offset) {
        return jpaQueryFactory.select(
                        projectionsConstructorOfProductQueryDto()
                ).from(product)
                .join(partner).on(partner.id.eq(product.partnerId))
                .where(
                        product.partnerId.eq(partnerId)
                                .and(allCategoryEq(categoryId, subCategoryId))
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

    private JPAQuery<ProductQueryDto> defaultQueryProductsByCategory(Long categoryId, Long subCategoryId) {
        return jpaQueryFactory.select(
                        projectionsConstructorOfProductQueryDto()
                ).from(product)
                .join(partner).on(partner.id.eq(product.partnerId))
                .where(
                        allCategoryEq(categoryId, subCategoryId)
                );
    }

    private ConstructorExpression<ProductQueryDto> projectionsConstructorOfProductQueryDto() {
        return Projections.constructor(ProductQueryDto.class,
                product.id, product.name,
                product.price, product.stock,
                product.score, product.salesVolume,
                product.detail,
                product.registerDate,
                product.thumbnail.storedFileName,
                product.thumbnail.viewFileName,
                partner.id, partner.name
        );
    }

    private BooleanExpression allCategoryEq(Long categoryId, Long subCategoryId) {
        return categoryEq(categoryId).and(subCategoryEq(subCategoryId));
    }

    private BooleanExpression categoryEq(Long categoryId){
        return categoryId == null ? null : product.categoryId.eq(categoryId);
    }

    private BooleanExpression subCategoryEq(Long subCategoryId){
        return subCategoryId == null ? null : product.subCategoryId.eq(subCategoryId);
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
