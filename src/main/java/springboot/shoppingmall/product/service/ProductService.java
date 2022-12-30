package springboot.shoppingmall.product.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.domain.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse saveProduct(ProductRequest productRequest){
        Category category = getCategory(productRequest.getCategoryId());
        Category subCategory = getCategory(productRequest.getSubCategoryId());

        Product product = productRepository.save(ProductRequest.toProduct(productRequest,category, subCategory));
        return ProductResponse.of(product);
    }

    private Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("카테고리가 존재하지 않습니다.")
        );
    }

    public ProductResponse findProduct(Long id){
        return ProductResponse.of(productRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
                ));
    }

    public List<ProductResponse> findProductsByCategory(Long categoryId, Long subCategoryId) {
        Category category = getCategory(categoryId);
        Category subCategory = getCategory(subCategoryId);
        return productRepository.findProductsByCategoryAndSubCategory(category, subCategory).stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}
