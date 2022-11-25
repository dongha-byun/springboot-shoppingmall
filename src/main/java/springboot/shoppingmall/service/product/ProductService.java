package springboot.shoppingmall.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.domain.product.Product;
import springboot.shoppingmall.dto.product.ProductRequest;
import springboot.shoppingmall.dto.product.ProductResponse;
import springboot.shoppingmall.repository.product.ProductRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Long saveProduct(ProductRequest productRequest){
        Product product = productRepository.save(Product.to(productRequest));
        return product.getId();
    }

    public ProductResponse findProduct(Long id){
        return ProductResponse.of(productRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 상품입니다.")
                ));
    }
}
