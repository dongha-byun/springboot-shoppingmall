package springboot.shoppingmall.product.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.product.domain.ProductQnaRepository;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.dto.ProductQnaRequest;
import springboot.shoppingmall.product.dto.ProductQnaResponse;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductQnaService {

    private final ProductQnaRepository productQnaRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ProductQnaResponse createQna(Long userId, Long productId, ProductQnaRequest productQnaRequest) {
        User writer = findUserById(userId);
        Product product = findProductById(productId);
        ProductQna productQna = productQnaRepository.save(ProductQna.builder()
                .content(productQnaRequest.getContent())
                .product(product)
                .writer(writer)
                .build());

        return ProductQnaResponse.of(productQna);
    }

    public List<ProductQnaResponse> findQnaAllByProduct(Long productId){
        Product product = findProductById(productId);
        return product.getQna().stream()
                .map(ProductQnaResponse::of)
                .collect(Collectors.toList());
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(
                        () -> new IllegalArgumentException("상품 조회 실패")
                );
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자 조회 실패")
                );
    }
}
