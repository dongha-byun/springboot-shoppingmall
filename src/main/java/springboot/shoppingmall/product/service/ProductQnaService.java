package springboot.shoppingmall.product.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.product.domain.ProductQnaRepository;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.product.dto.ProductQnaDto;
import springboot.shoppingmall.product.dto.ProductQnaRequest;
import springboot.shoppingmall.product.dto.ProductQnaResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductQnaService {

    private final ProductQnaRepository productQnaRepository;
    private final ProductFinder productFinder;

    @Transactional
    public ProductQnaResponse createQna(Long userId, String loginId, Long productId, ProductQnaRequest productQnaRequest) {
        Product product = productFinder.findProductById(productId);
        ProductQna productQna = productQnaRepository.save(ProductQna.builder()
                .content(productQnaRequest.getContent())
                .product(product)
                .writerId(userId)
                .build());

        return ProductQnaResponse.of(productQna);
    }

    public List<ProductQnaResponse> findQnaAllByProduct(Long productId){
        List<ProductQnaDto> qnaDtos = productQnaRepository.findAllProductQna(productId);
        return qnaDtos.stream()
                .map(ProductQnaResponse::of)
                .collect(Collectors.toList());
    }

    public ProductQnaResponse findQnaByProduct(Long productId, Long qnaId){
        Product product = productFinder.findProductById(productId);
        ProductQna productQna = product.findQna(qnaId);
        return ProductQnaResponse.of(productQna);
    }
}
