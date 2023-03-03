package springboot.shoppingmall.product.query.repository;

import static springboot.shoppingmall.product.domain.QProduct.*;
import static springboot.shoppingmall.product.query.ProductQueryOrderType.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.query.ProductQueryOrderType;

public class CustomProductQueryRepositoryImpl implements CustomProductQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;
    public CustomProductQueryRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Product> queryProducts(Category category, Category subCategory, ProductQueryOrderType orderType) {
        return defaultQueryProductsByCategory(category, subCategory)
                .orderBy(orderBy(orderType))
                .fetch();
    }

    @Override
    public List<Product> searchProducts(Category category, Category subCategory, String searchKeyword) {
        return defaultQueryProductsByCategory(category, subCategory)
                .where(
                        containSearchKeyword(searchKeyword)
                ).fetch();
    }

    @Override
    public List<Product> queryProducts(Category category, Category subCategory, ProductQueryOrderType orderType,
                                       int limit, int offset) {
        return defaultQueryProductsByCategory(category, subCategory)
                .orderBy(orderBy(orderType))
                .limit(limit)
                .offset(offset)
                .fetch();
    }

    private BooleanExpression containSearchKeyword(String searchText) {
        return product.name.contains(searchText);
    }

    private JPAQuery<Product> defaultQueryProductsByCategory(Category category, Category subCategory) {
        return jpaQueryFactory.selectFrom(product)
                .where(
                        allCategoryEq(category, subCategory)
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
