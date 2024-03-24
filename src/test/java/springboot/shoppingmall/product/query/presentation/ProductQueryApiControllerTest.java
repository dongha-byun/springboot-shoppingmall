package springboot.shoppingmall.product.query.presentation;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.authorization.GatewayConstants;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.product.query.application.ProductQueryService;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;

@WebMvcTest(controllers = ProductQueryApiController.class)
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
                getProductQueryDto(
                        10L, "상품 1", 1200, 10, 1.5,
                        100,
                        LocalDateTime.of(2023, 9, 1, 12, 12, 12)
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

    @Test
    @DisplayName("특정 카테고리의 상품들을 조회한다.")
    void find_product_by_category() throws Exception {
        // given
        LocalDateTime registerDate = LocalDateTime.of(2023, 8, 1, 0, 0, 0);
        List<ProductQueryDto> products = Arrays.asList(
                getProductQueryDto(10L, "상품 1", 1200, 10, 1.5, 100, registerDate.minusDays(1)),
                getProductQueryDto(20L, "상품 2", 2200, 50, 3.5, 200, registerDate.plusDays(1)),
                getProductQueryDto(30L, "상품 3", 3200, 12, 2.5, 190, registerDate.plusDays(4)),
                getProductQueryDto(40L, "상품 4", 4900, 20, 2.0, 330, registerDate.plusDays(7))
        );
        when(categoryFinder.findById(1L)).thenReturn(new Category("상위 카테고리"));
        when(categoryFinder.findById(11L)).thenReturn(new Category("하위 카테고리"));
        when(productQueryService.findProductByOrder(anyLong(), anyLong(), any(), anyInt(), anyInt())).thenReturn(
                products
        );
        when(productQueryService.getTotalCount(anyLong(), anyLong())).thenReturn(products.size());

        // when & then
        mockMvc.perform(
                        get("/products"
                                + "?categoryId={categoryId}"
                                + "&subCategoryId={subCategoryId}"
                                + "&orderType={orderType}"
                                + "&limit={limit}"
                                + "&offset={offset}",
                                1L, 11L, "RECENT", 10, 0
                        )
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount", is(4)))
                .andExpect(jsonPath("$.categoryName", is("상위 카테고리")))
                .andExpect(jsonPath("$.subCategoryName", is("하위 카테고리")))
                .andExpect(jsonPath("$.data", hasSize(4)))
                .andExpect(jsonPath("$.data[*].name", contains(
                        "상품 1", "상품 2", "상품 3", "상품 4"
                )));
    }

    @Test
    @DisplayName("상품 명을 검색한다.")
    void search_products() throws Exception {
        // given
        LocalDateTime registerDate = LocalDateTime.of(2023, 8, 5, 0, 0, 0);
        List<ProductQueryDto> products = Arrays.asList(
                getProductQueryDto(10L, "상품 1", 1200, 10, 1.5, 100, registerDate.minusDays(1)),
                getProductQueryDto(20L, "상품 2", 2200, 50, 3.5, 200, registerDate.plusDays(1)),
                getProductQueryDto(30L, "상품 3", 3200, 12, 2.5, 190, registerDate.plusDays(4))
        );
        when(productQueryService.searchProducts(any(), any(), anyInt(), anyInt())).thenReturn(
                products
        );

        // when & then
        mockMvc.perform(
                        get("/search-products"
                                        + "?searchKeyword={searchKeyword}"
                                        + "&orderType={orderType}"
                                        + "&limit={limit}"
                                        + "&offset={offset}",
                                "상품", "RECENT", 10, 0
                        )
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(3)));
    }

    @Test
    @DisplayName("판매자가 자신이 등록한 상품들을 조회한다.")
    void find_partners_products() throws Exception {
        // given
        LocalDateTime registerDate = LocalDateTime.of(2023, 8, 1, 0, 0, 0);
        List<ProductQueryDto> products = Arrays.asList(
                getProductQueryDto(10L, "상품 1", 1200, 10, 1.5, 100, registerDate.minusDays(1)),
                getProductQueryDto(20L, "상품 2", 2200, 20, 2.5, 150, registerDate.minusDays(2)),
                getProductQueryDto(30L, "상품 3", 3200, 30, 3.5, 200, registerDate.minusDays(3)),
                getProductQueryDto(40L, "상품 4", 4200, 40, 4.5, 170, registerDate.minusDays(4)),
                getProductQueryDto(50L, "상품 5", 5200, 50, 5.0, 120, registerDate.minusDays(5))
        );

        when(categoryFinder.findById(1L)).thenReturn(new Category("상위 카테고리"));
        when(categoryFinder.findById(11L)).thenReturn(new Category("하위 카테고리"));
        when(productQueryService.findPartnersProductsAll(any(), anyLong(), anyLong(), anyInt(), anyInt())).thenReturn(
                products
        );
        when(productQueryService.countPartnersProducts(any(), anyLong(), anyLong())).thenReturn(products.size());

        // when & then
        mockMvc.perform(
                        get("/partners/products"
                                + "?categoryId={categoryId}"
                                + "&subCategoryId={subCategoryId}"
                                + "&limit={limit}"
                                + "&offset={offset}",
                                1, 11, 10, 0)
                                .header(GatewayConstants.GATEWAY_HEADER, 100)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount", is(5)));
    }

    private ProductQueryDto getProductQueryDto(
            Long id, String name, int price, int quantity, double score, int salesVolume,
            LocalDateTime registerDate
    ) {

        Long partnersId = 100L;
        String partnersName = "파트너즈";
        String storedFileName = "stored-file-name-" + name;
        String viewFileName = "view-file-name-" + name;
        String detail = name + "입니다!";
        return new ProductQueryDto(
                id, name, price, quantity, score, salesVolume, detail,
                registerDate, storedFileName, viewFileName,
                partnersId, partnersName
        );
    }
}