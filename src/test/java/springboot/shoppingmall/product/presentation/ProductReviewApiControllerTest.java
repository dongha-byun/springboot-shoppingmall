package springboot.shoppingmall.product.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import springboot.shoppingmall.product.application.ProductReviewService;
import springboot.shoppingmall.product.application.dto.ProductReviewDto;
import springboot.shoppingmall.product.application.dto.ProductUserReviewDto;
import springboot.shoppingmall.product.configuration.TestFileConfiguration;
import springboot.shoppingmall.product.presentation.request.ProductReviewRequest;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@Import({TestFileConfiguration.class})
@WebMvcTest(
        controllers = ProductReviewApiController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                PartnersConfiguration.class
        })
)
class ProductReviewApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductReviewService productReviewService;

    @Test
    @DisplayName("상품에 리뷰를 작성한다.")
    void create_review() throws Exception {
        // given
        ProductReviewRequest productReviewRequest = new ProductReviewRequest("리뷰 입니다.", 5);
        String content = objectMapper.writeValueAsString(productReviewRequest);
        MockMultipartFile data = makeMockMultipartData(content);

        when(productReviewService.createProductReview(any(), any(), any(), any(), any())).thenReturn(
                new ProductUserReviewDto(
                        1L, "리뷰 입니다.", 5, "상품 1",
                        LocalDateTime.of(2023, 7, 19, 0, 0, 0),
                        new ArrayList<>()
                )
        );

        // when & then
        mockMvc.perform(multipart("/orders/{orderId}/{orderItemId}/products/{productId}/reviews",
                        1L, 11L, 1L)
                        .file(data)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.images", hasSize(0)));
    }

    @Test
    @DisplayName("리뷰 등록 시, 이미지는 5개까지 첨부할 수 있다.")
    void create_review_with_image_max_five() throws Exception {
        // given
        ProductReviewRequest productReviewRequest = new ProductReviewRequest("리뷰 입니다.", 5);
        String content = objectMapper.writeValueAsString(productReviewRequest);
        MockMultipartFile data = makeMockMultipartData(content);
        MockMultipartFile file1 = makeMockMultipartFile("stored-file-name-1.png");
        MockMultipartFile file2 = makeMockMultipartFile("stored-file-name-2.png");
        MockMultipartFile file3 = makeMockMultipartFile("stored-file-name-3.png");
        MockMultipartFile file4 = makeMockMultipartFile("stored-file-name-4.png");
        MockMultipartFile file5 = makeMockMultipartFile("stored-file-name-5.png");
        MockMultipartFile file6 = makeMockMultipartFile("stored-file-name-6.png");

        // when & then
        mockMvc.perform(multipart("/orders/{orderId}/{orderItemId}/products/{productId}/reviews",
                        1L, 11L, 1L)
                        .file(data)
                        .file(file1)
                        .file(file2)
                        .file(file3)
                        .file(file4)
                        .file(file5)
                        .file(file6)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("이미지는 최대 5장까지 첨부할 수 있습니다.")));

    }

    @Test
    @DisplayName("상품에 등록된 리뷰들을 조회한다.")
    void find_all_reviews_by_product() throws Exception {
        // given
        LocalDateTime writeDate = LocalDateTime.of(2023, 7, 29, 0, 0, 0);
        List<ProductReviewDto> reviews = Arrays.asList(
                new ProductReviewDto(10L, "리뷰1 입니다.", 3, writeDate.plusDays(1)),
                new ProductReviewDto(20L, "리뷰2 입니다.", 4, writeDate.plusDays(2)),
                new ProductReviewDto(30L, "리뷰3 입니다.", 2, writeDate.plusDays(3)),
                new ProductReviewDto(40L, "리뷰4 입니다.", 3, writeDate.plusDays(4))
        );

        when(productReviewService.findAllReview(any())).thenReturn(reviews);

        // when & then
        mockMvc.perform(
                        get("/products/{id}/reviews", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$..id", contains(
                        10, 20, 30, 40
                )));
    }

    @Test
    @DisplayName("구매자가 자신이 작성했던 리뷰 목록을 조회한다.")
    void find_all_reviews_of_user() throws Exception {
        // given
        LocalDateTime writeDate = LocalDateTime.of(2023, 9, 1, 0, 0, 0);
        List<ProductUserReviewDto> reviews = Arrays.asList(
                new ProductUserReviewDto(10L, "리뷰1 입니다.", 3, "상품1", writeDate.plusDays(1), new ArrayList<>()),
                new ProductUserReviewDto(20L, "리뷰2 입니다.", 4, "상품2", writeDate.plusDays(2), new ArrayList<>()),
                new ProductUserReviewDto(30L, "리뷰3 입니다.", 2, "상품3", writeDate.plusDays(3), new ArrayList<>()),
                new ProductUserReviewDto(40L, "리뷰4 입니다.", 5, "상품4", writeDate.plusDays(4), new ArrayList<>())
        );
        when(productReviewService.findAllUserReview(any())).thenReturn(reviews);
        
        // when & then
        mockMvc.perform(get("/users/reviews"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$..id", contains(
                        10, 20, 30, 40
                )));
        
    }

    private MockMultipartFile makeMockMultipartData(String content) {
        return new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                content.getBytes()
        );
    }

    private MockMultipartFile makeMockMultipartFile(String originalFilename) {
        return new MockMultipartFile(
                "file",
                originalFilename,
                MediaType.TEXT_PLAIN_VALUE,
                "test-image-file-content-with-string".getBytes()
        );
    }
}