package springboot.shoppingmall.product.query.service;

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
    private final CategoryFinder categoryFinder;

    public List<ProductQueryDto> findProductByOrder(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.queryProducts(category, subCategory, orderType);
    }

    public List<ProductQueryDto> findProductByOrder(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType,
                                                         int limit, int offset) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.queryProducts(category, subCategory, orderType, limit, offset);
    }

    public int getTotalCount(Long categoryId, Long subCategoryId) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.countByCategoryAndSubCategory(category, subCategory);
    }

    public List<ProductQueryDto> searchProducts(String searchKeyword, ProductQueryOrderType orderType,
                                                int limit, int offset) {
        return productQueryRepository.searchProducts(searchKeyword, orderType, limit, offset);
    }

    public List<ProductQueryDto> findPartnersProductsAll(Long partnerId, Long categoryId, Long subCategoryId,
                                                              int limit, int offset) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.queryPartnersProducts(partnerId, category, subCategory, limit, offset);
    }

    public int countPartnersProducts(Long partnerId, Long categoryId, Long subCategoryId) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.countByPartnerIdAndCategoryAndSubCategory(partnerId, category, subCategory);
    }

    public ProductQueryDto findProductOf(Long productId) {
        return productQueryRepository.findProductOf(productId);
    }
}
