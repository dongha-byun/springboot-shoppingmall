package springboot.shoppingmall.product.query.repository;

import static springboot.shoppingmall.product.domain.QProduct.*;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.QProduct;
import springboot.shoppingmall.product.query.ProductQueryOrderType;

@RequiredArgsConstructor
public class CustomProductQueryRepositoryImpl implements CustomProductQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Product> queryProducts(Category category, Category subCategory) {
        return jpaQueryFactory.selectFrom(product)
                .where(
                        product.category.eq(category).and(
                                product.subCategory.eq(subCategory)
                        )
                )
                .fetch();
    }

    @Override
    public List<Product> queryProducts(Category category, Category subCategory, ProductQueryOrderType orderType) {
        return jpaQueryFactory.selectFrom(product)
                .where(
                        allCategoryEq(category, subCategory)
                ).orderBy(orderBy(orderType))
                .fetch();
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
        if(orderType == ProductQueryOrderType.SCORE) {
            return product.score.desc();
        }

        if(orderType == ProductQueryOrderType.RECENT) {
            return product.registerDate.desc();
        }

        if(orderType == ProductQueryOrderType.PRICE) {
            return product.price.asc();
        }

        return null;
    }
}
