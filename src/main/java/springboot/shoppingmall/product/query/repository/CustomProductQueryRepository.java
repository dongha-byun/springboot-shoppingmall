package springboot.shoppingmall.product.query.repository;

import java.util.List;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;

public interface CustomProductQueryRepository {
    List<ProductQueryDto> queryProducts(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType);

    List<ProductQueryDto> searchProducts(Long categoryId, Long subCategoryId, String searchKeyword);

    List<ProductQueryDto> queryProducts(Long categoryId, Long subCategoryId, ProductQueryOrderType recent, int limit, int offset);

    List<ProductQueryDto> queryPartnersProducts(Long partnerId, Long categoryId, Long subCategoryId, int limit, int offset);

    List<ProductQueryDto> searchProducts(String searchKeyword, ProductQueryOrderType orderType, int limit, int offset);

    ProductQueryDto findProductOf(Long productId);
}
