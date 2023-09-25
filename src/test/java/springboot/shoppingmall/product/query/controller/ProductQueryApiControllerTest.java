package springboot.shoppingmall.product.query.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;
import springboot.shoppingmall.product.query.service.ProductQueryService;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = ProductQueryApiController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        PartnersConfiguration.class,
                        AuthenticationConfig.class
                }
        )
)
class ProductQueryApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductQueryService productQueryService;

    @MockBean
    CategoryFinder categoryFinder;

    @Test
    @DisplayName("특정 상품을 조회한다.")
    void find_product() throws Exception {
        // given
        when(productQueryService.findProductOf(any())).thenReturn(
                new ProductQueryDto(
                        10L, "상품 1", 12000, 2, 3.0, 100,
                        LocalDateTime.of(2023, 7, 12, 15, 0, 0),
                        "stored-file-name", "view-file-name",
                        100L, "판매자 1"
                )
        );

        // when & then
        mockMvc.perform(
                        get("/products/{id}", 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.partnersId", is(100)));
    }
}