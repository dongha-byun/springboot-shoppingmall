package springboot.shoppingmall.product.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.authorization.configuration.AuthenticationConfig;
import springboot.shoppingmall.category.dto.CategoryDto;
import springboot.shoppingmall.product.application.ProductService;
import springboot.shoppingmall.product.application.dto.ProductDto;
import springboot.shoppingmall.product.configuration.TestFileConfiguration;
import springboot.shoppingmall.product.presentation.request.ProductRequest;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@Import({TestFileConfiguration.class})
@WebMvcTest(
        controllers = ProductApiController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                AuthenticationConfig.class,
                PartnersConfiguration.class
        })
)
class ProductApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("상품을 등록한다.")
    void create_product() throws Exception {
        // given
        ProductRequest productRequest = new ProductRequest("상품 1", 12000, 2, 1L, 11L, "상품 설명입니다.");
        String content = objectMapper.writeValueAsString(productRequest);
        String imgFile = "test-image-file-content-with-string";

        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                content.getBytes()
        );
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "stored-file-name.png",
                MediaType.TEXT_PLAIN_VALUE,
                imgFile.getBytes()
        );

        when(productService.saveProduct(any(), any())).thenReturn(
                new ProductDto(
                        10L, "product-code-1", "상품 1",
                        12000, 2, "상품 설명입니다.",
                        new CategoryDto(1L, "상위 카테고리"),
                        new CategoryDto(11L, "하위 카테고리", 1L),
                        100L, "stored-file-name", "view-file-name")
        );

        // when & then
        mockMvc.perform(
                        multipart("/products")
                                .file(data)
                                .file(file)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    @DisplayName("상품의 대표 이미지가 없으면, 상품 등록에 실패한다.")
    void create_product_fail_with_no_image() throws Exception {
        // given
        ProductRequest productRequest = new ProductRequest("상품 1", 12000, 2, 1L, 11L, "상품 설명입니다.");
        String content = objectMapper.writeValueAsString(productRequest);

        MockMultipartFile data = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                content.getBytes()
        );

        // when & then
        mockMvc.perform(
                        multipart("/products")
                                .file(data)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}