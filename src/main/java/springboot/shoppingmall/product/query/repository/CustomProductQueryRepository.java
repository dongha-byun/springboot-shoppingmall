package springboot.shoppingmall.product.query.repository;

import java.util.List;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.query.ProductQueryOrderType;

public interface CustomProductQueryRepository {

    List<Product> queryProducts(Category category, Category subCategory);
    List<Product> queryProducts(Category category, Category subCategory, ProductQueryOrderType orderType);
}
