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
import springboot.shoppingmall.product.application.ProductQnaService;
import springboot.shoppingmall.product.application.dto.ProductQnaDto;
import springboot.shoppingmall.product.presentation.request.ProductQnaRequest;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = ProductQnaApiController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                PartnersConfiguration.class
        })
)
class ProductQnaApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductQnaService productQnaService;

    @Test
    @DisplayName("특정 상품에 문의를 등록한다.")
    void create_qna() throws Exception {
        // given
        ProductQnaRequest productQnaRequest = new ProductQnaRequest("문의드립니다.");
        String content = objectMapper.writeValueAsString(productQnaRequest);

        when(productQnaService.createQna(any(), any(), any())).thenReturn(
                new ProductQnaDto(
                        10L, "문의드립니다.",
                        LocalDateTime.of(2023, 7, 17, 13, 14, 0),
                        null
                )
        );

        // when & then
        mockMvc.perform(post("/products/{id}/qna", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)));
    }

    @Test
    @DisplayName("문의내용은 필수로 입력해야 한다.")
    void required_content() throws Exception {
        // given
        ProductQnaRequest productQnaRequest = new ProductQnaRequest("");
        String content = objectMapper.writeValueAsString(productQnaRequest);

        // when & then
        mockMvc.perform(post("/products/{id}/qna", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "내용은 필수항목 입니다."
                )));
    }
}