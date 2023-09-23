package springboot.shoppingmall.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.product.application.dto.ProductQnaAnswerDto;
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.product.domain.ProductQnaAnswer;
import springboot.shoppingmall.product.domain.ProductQnaAnswerRepository;
import springboot.shoppingmall.product.domain.ProductQnaRepository;

@RequiredArgsConstructor
@Service
public class ProductQnaAnswerService {

    private final ProductQnaRepository productQnaRepository;
    private final ProductQnaAnswerRepository productQnaAnswerRepository;

    @Transactional
    public ProductQnaAnswerDto createQnaAnswer(Long qnaId, String content) {
        ProductQna productQna = findQnaById(qnaId);
        ProductQnaAnswer answer = ProductQnaAnswer.createQnaAnswer(content, productQna);

        return ProductQnaAnswerDto.of(productQnaAnswerRepository.save(answer));
    }

    private ProductQna findQnaById(Long qnaId) {
        return productQnaRepository.findById(qnaId)
                .orElseThrow(
                        () -> new IllegalArgumentException("상품 문의글 조회 실패")
                );
    }
}
