package springboot.shoppingmall.product.query.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryResponse;
import springboot.shoppingmall.product.query.repository.ProductQueryRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductQueryResponse> findProductByOrder(Long categoryId, Long subCategoryId, ProductQueryOrderType orderType) {
        Category category = getCategory(categoryId);
        Category subCategory = getCategory(subCategoryId);
        return productQueryRepository.queryProducts(category, subCategory, orderType).stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductQueryResponse> searchProducts(Long categoryId, Long subCategoryId, String searchKeyword) {
        Category category = getCategory(categoryId);
        Category subCategory = getCategory(subCategoryId);
        return productQueryRepository.searchProducts(category, subCategory, searchKeyword).stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("카테고리가 존재하지 않습니다.")
        );
    }
}