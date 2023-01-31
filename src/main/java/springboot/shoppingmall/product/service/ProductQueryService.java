package springboot.shoppingmall.product.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.repository.ProductQueryRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductResponse> findProductsByCategory(Long categoryId, Long subCategoryId) {
        Category category = getCategory(categoryId);
        Category subCategory = getCategory(subCategoryId);
        return productQueryRepository.queryProducts(category, subCategory).stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findProductByOrder(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType) {
        Category category = getCategory(categoryId);
        Category subCategory = getCategory(subCategoryId);
        return productQueryRepository.queryProducts(category, subCategory, orderType).stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(Long categoryId, Long subCategoryId, String searchKeyword) {
        Category category = getCategory(categoryId);
        Category subCategory = getCategory(subCategoryId);
        return productQueryRepository.searchProducts(category, subCategory, searchKeyword).stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("카테고리가 존재하지 않습니다.")
        );
    }
}
