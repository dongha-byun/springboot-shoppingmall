package springboot.shoppingmall.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.user.domain.User;

public class ProductTest {

    @Test
    @DisplayName("Qna 조회 테스트")
    void findQnaTest() {
        // given
        User user = new User("테스터", "tester1", "tester1!", "010-1234-1234");
        Product product = new Product(1L, "상품1", 12000, 22, 0.0, 0, LocalDateTime.now(), new Category("상위 카테고리"), new Category("하위 카테고리"));
        ProductQna productQna1 = new ProductQna(1L, "문의 입니다 1", product, user.getId());
        ProductQna productQna2 = new ProductQna(2L, "문의 입니다 1", product, user.getId());

        // when
        ProductQna findQna1 = product.findQna(1L);
        ProductQna findQna2 = product.findQna(2L);

        // then
        assertThat(findQna1).isEqualTo(productQna1);
        assertThat(findQna2).isEqualTo(productQna2);
    }

    @Test
    @DisplayName("판매량 증가 테스트")
    void increaseSalesVolumeTest() {
        // given
        Product product = new Product(1L, "상품1", 12000, 22, 0.0, 0, LocalDateTime.now(), new Category("상위 카테고리"), new Category("하위 카테고리"));

        // when
        product.increaseSalesVolume();

        // then
        assertThat(product.getSalesVolume()).isEqualTo(1);
    }
}
