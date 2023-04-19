package springboot.shoppingmall.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.domain.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryFinder categoryFinder;
    private final ProductFinder productFinder;

    @Transactional
    public ProductResponse saveProduct(Long partnerId, ProductRequest productRequest){
        Category category = categoryFinder.findById(productRequest.getCategoryId());
        Category subCategory = categoryFinder.findById(productRequest.getSubCategoryId());

        Product product = productRepository.save(ProductRequest.toProduct(productRequest,category, subCategory, partnerId));
        return ProductResponse.of(product);
    }

    public ProductResponse findProduct(Long id){
        return ProductResponse.of(productFinder.findProductById(id));
    }
}
