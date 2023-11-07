package springboot.shoppingmall.product.query.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.product.query.dto.PartnersProductQnaDto;
import springboot.shoppingmall.product.query.application.PartnerProductQnaQueryService;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = PartnersProductQnaQueryController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        PartnersConfiguration.class
                }
        )
)
class PartnersProductQnaQueryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PartnerProductQnaQueryService partnerProductQnaQueryService;

    @Test
    @DisplayName("판매자가 자신이 등록한 상품에 대한 문의글들을 조회한다.")
    void find_partners_product_qnas() throws Exception {
        // given
        LocalDateTime writeDate = LocalDateTime.of(2023, 8, 12, 0, 0, 0);
        when(partnerProductQnaQueryService.findPartnersProductQna(any(), any())).thenReturn(
                Arrays.asList(
                        new PartnersProductQnaDto(10L, "문의 합니다.1", 1L, "상품 1", "image1", writeDate.plusDays(1), false),
                        new PartnersProductQnaDto(20L, "문의 합니다.2", 2L, "상품 2", "image2", writeDate.plusDays(2), true),
                        new PartnersProductQnaDto(30L, "문의 합니다.3", 3L, "상품 3", "image3", writeDate.plusDays(3), false)
                )
        );

        // when & then
        mockMvc.perform(
                        get("/partners/product/qnas"
                                + "?isAnswered={isAnswered}",
                                "ALL")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)));
    }

}