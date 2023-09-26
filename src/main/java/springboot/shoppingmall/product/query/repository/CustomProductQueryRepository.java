package springboot.shoppingmall.product.query.repository;

import java.util.List;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;

public interface CustomProductQueryRepository {
    List<ProductQueryDto> queryProducts(Category category, Category subCategory, ProductQueryOrderType orderType);

    List<ProductQueryDto> searchProducts(Category category, Category subCategory, String searchKeyword);

    List<ProductQueryDto> queryProducts(Category category, Category subCategory, ProductQueryOrderType recent, int limit, int offset);

    List<ProductQueryDto> queryPartnersProducts(Long partnerId, Category category, Category subCategory, int limit, int offset);

    List<ProductQueryDto> searchProducts(String searchKeyword, ProductQueryOrderType orderType, int limit, int offset);

    ProductQueryDto findProductOf(Long productId);
}
