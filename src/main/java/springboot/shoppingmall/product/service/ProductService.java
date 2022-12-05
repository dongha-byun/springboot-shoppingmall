package springboot.shoppingmall.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.domain.ProductRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Long saveProduct(ProductRequest productRequest){
        Product product = productRepository.save(ProductRequest.toProduct(productRequest));
        return product.getId();
    }

    public ProductResponse findProduct(Long id){
        return ProductResponse.of(productRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
                ));
    }
}
