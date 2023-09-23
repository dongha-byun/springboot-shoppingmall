package springboot.shoppingmall.product.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.authorization.configuration.AuthenticationConfig;
import springboot.shoppingmall.product.application.ProductQnaService;
import springboot.shoppingmall.product.presentation.request.ProductReviewRequest;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = ProductReviewApiController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                AuthenticationConfig.class,
                PartnersConfiguration.class
        })
)
class ProductReviewApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductQnaService productQnaService;

    @Test
    @DisplayName("상품에 리뷰를 작성한다.")
    void create_review() throws Exception {
        // given
        ProductReviewRequest productReviewRequest = new ProductReviewRequest();

        // when & then
        mockMvc.perform(post("/products/{productId}/qna/{qnaId}/answer", 10L, 1010L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}