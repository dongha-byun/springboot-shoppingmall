package springboot.shoppingmall.product.service;

import java.util.List;
import java.util.stream.Collectors;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.dto.ProductResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductQueryService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public List<ProductResponse> findProductsByCategory(Long categoryId, Long subCategoryId) {
        Category category = getCategory(categoryId);
        Category subCategory = getCategory(subCategoryId);
        return productRepository.findProductsByCategoryAndSubCategory(category, subCategory).stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("카테고리가 존재하지 않습니다.")
        );
    }
}
