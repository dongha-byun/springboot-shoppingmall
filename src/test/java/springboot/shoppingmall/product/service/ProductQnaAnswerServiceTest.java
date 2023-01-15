package springboot.shoppingmall.product.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.product.domain.ProductQnaAnswer;
import springboot.shoppingmall.product.domain.ProductQnaAnswerRepository;
import springboot.shoppingmall.product.domain.ProductQnaRepository;
import springboot.shoppingmall.product.dto.ProductQnaAnswerResponse;
import springboot.shoppingmall.user.domain.User;

@ExtendWith(MockitoExtension.class)
public class ProductQnaAnswerServiceTest {

    @Mock
    ProductQnaRepository productQnaRepository;

    @Mock
    ProductQnaAnswerRepository productQnaAnswerRepository;

    @Test
    @DisplayName("상품 문의의 답변을 추가한다.")
    void createTest(){
        // given
        // 문의 답변 내용을 저장한다.
        // 상품 Id, 문의 Id 가 넘어온다. -> 문의만 해볼까? 어차피 OneToOne
        Long qnaId = 1L;
        String content = "문의에 대한 답변 입니다.";
        Product product = new Product(0L, "상품1", 10000, 22, 0.0, new Category("상위 카테고리"), new Category("하위 카테고리"));
        User user = new User("문의작성자1", "qnaWriter1", "qnaWriter1!", "010-2222-3333");
        ProductQnaAnswerService productQnaAnswerService = new ProductQnaAnswerService(productQnaRepository, productQnaAnswerRepository);
        when(productQnaRepository.findById(any())).thenReturn(Optional.of(new ProductQna(content, product, user)));
        when(productQnaAnswerRepository.save(any())).thenReturn(new ProductQnaAnswer(content));

        // when
        ProductQnaAnswerResponse qnaAnswer = productQnaAnswerService.createQnaAnswer(qnaId, content);

        // then
        assertThat(qnaAnswer).isNotNull();
        assertThat(qnaAnswer.getContent()).contains(content);
    }
}
