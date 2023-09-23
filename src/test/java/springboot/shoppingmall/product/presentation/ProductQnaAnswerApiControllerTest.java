package springboot.shoppingmall.product.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
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
import springboot.shoppingmall.product.application.ProductQnaAnswerService;
import springboot.shoppingmall.product.application.dto.ProductQnaAnswerDto;
import springboot.shoppingmall.product.presentation.request.ProductQnaAnswerRequest;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = ProductQnaAnswerApiController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        AuthenticationConfig.class,
                        PartnersConfiguration.class
                })
)
class ProductQnaAnswerApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductQnaAnswerService productQnaAnswerService;

    @Test
    @DisplayName("상품 문의에 답변을 등록한다.")
    void create_qna_answer() throws Exception {
        // given
        ProductQnaAnswerRequest productQnaAnswerRequest = new ProductQnaAnswerRequest("답변 입니다.");
        String content = objectMapper.writeValueAsString(productQnaAnswerRequest);

        when(productQnaAnswerService.createQnaAnswer(any(), any())).thenReturn(
                new ProductQnaAnswerDto(
                        100L, "답변 입니다.",
                        LocalDateTime.of(2023, 7, 12, 10, 0, 0)
                )
        );

        // when & then
        mockMvc.perform(post("/products/{productId}/qna/{qnaId}/answer", 10L, 1010L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(100)));
    }

    @Test
    @DisplayName("답변 내용을 필수로 입력해야 한다.")
    void required_content() throws Exception {
        // given
        ProductQnaAnswerRequest productQnaAnswerRequest = new ProductQnaAnswerRequest();
        String content = objectMapper.writeValueAsString(productQnaAnswerRequest);

        // when & then
        mockMvc.perform(post("/products/{productId}/qna/{qnaId}/answer", 10L, 1010L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}