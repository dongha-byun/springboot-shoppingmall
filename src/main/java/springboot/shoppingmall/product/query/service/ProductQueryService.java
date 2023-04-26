package springboot.shoppingmall.product.query.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryResponse;
import springboot.shoppingmall.product.query.repository.ProductQueryRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;
    private final CategoryFinder categoryFinder;

    public List<ProductQueryResponse> findProductByOrder(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.queryProducts(category, subCategory, orderType).stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductQueryResponse> findProductByOrder(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType,
                                                         int limit, int offset) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.queryProducts(category, subCategory, orderType, limit, offset).stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
    }

    public int getTotalCount(Long categoryId, Long subCategoryId) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.countByCategoryAndSubCategory(category, subCategory);
    }

    public List<ProductQueryResponse> searchProducts(Long categoryId, Long subCategoryId, String searchKeyword) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.searchProducts(category, subCategory, searchKeyword).stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductQueryResponse> findPartnersProductsAll(Long partnerId, Long categoryId, Long subCategoryId,
                                                              int limit, int offset) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.queryPartnersProducts(partnerId, category, subCategory, limit, offset).stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
    }

    public int countPartnersProducts(Long partnerId, Long categoryId, Long subCategoryId) {
        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        return productQueryRepository.countByPartnerIdAndCategoryAndSubCategory(partnerId, category, subCategory);
    }
}
