package springboot.shoppingmall.product.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductFinder {
    private final ProductRepository productRepository;

    public Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(
                        () -> new IllegalArgumentException("상품 조회 실패")
                );
    }
}
