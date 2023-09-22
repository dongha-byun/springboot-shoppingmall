package springboot.shoppingmall.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.product.domain.ProductQnaAnswer;
import springboot.shoppingmall.product.domain.ProductQnaAnswerRepository;
import springboot.shoppingmall.product.domain.ProductQnaRepository;
import springboot.shoppingmall.product.presentation.response.ProductQnaAnswerResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductQnaAnswerService {

    private final ProductQnaRepository productQnaRepository;
    private final ProductQnaAnswerRepository productQnaAnswerRepository;

    @Transactional
    public ProductQnaAnswerResponse createQnaAnswer(Long qnaId, String content) {
        ProductQna productQna = findQnaById(qnaId);
        ProductQnaAnswer answer = ProductQnaAnswer.createQnaAnswer(content, productQna);

        return ProductQnaAnswerResponse.of(productQnaAnswerRepository.save(answer));
    }

    private ProductQna findQnaById(Long qnaId) {
        return productQnaRepository.findById(qnaId)
                .orElseThrow(
                        () -> new IllegalArgumentException("상품 문의글 조회 실패")
                );
    }
}