package springboot.shoppingmall.product.query.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;
import springboot.shoppingmall.product.query.repository.ProductQueryRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;

    public List<ProductQueryDto> findProductByOrder(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType) {
        return productQueryRepository.queryProducts(categoryId, subCategoryId, orderType);
    }

    public List<ProductQueryDto> findProductByOrder(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType,
                                                         int limit, int offset) {
        return productQueryRepository.queryProducts(categoryId, subCategoryId, orderType, limit, offset);
    }

    public int getTotalCount(Long categoryId, Long subCategoryId) {
        return productQueryRepository.countByCategoryIdAndSubCategoryId(categoryId, subCategoryId);
    }

    public List<ProductQueryDto> searchProducts(String searchKeyword, ProductQueryOrderType orderType,
                                                int limit, int offset) {
        return productQueryRepository.searchProducts(searchKeyword, orderType, limit, offset);
    }

    public List<ProductQueryDto> findPartnersProductsAll(Long partnerId, Long categoryId, Long subCategoryId,
                                                              int limit, int offset) {
        return productQueryRepository.queryPartnersProducts(partnerId, categoryId, subCategoryId, limit, offset);
    }

    public int countPartnersProducts(Long partnerId, Long categoryId, Long subCategoryId) {
        return productQueryRepository.countByPartnerIdAndCategoryIdAndSubCategoryId(partnerId, categoryId, subCategoryId);
    }

    public ProductQueryDto findProductOf(Long productId) {
        return productQueryRepository.findProductOf(productId);
    }
}
