package springboot.shoppingmall.delivery.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.delivery.application.DeliveryService;
import springboot.shoppingmall.delivery.presentation.request.DeliveryRequest;
import springboot.shoppingmall.delivery.presentation.response.DeliveryResponse;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = DeliveryController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {
                        PartnersConfiguration.class
                })
)
class DeliveryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DeliveryService deliveryService;

    @Test
    @DisplayName("사용자가 자신이 등록한 배송지 목록을 조회한다.")
    void find_all_delivery() throws Exception {
        // given
        when(deliveryService.findAllDelivery(any())).thenReturn(
                Arrays.asList(
                        new DeliveryResponse(
                                1L, "배송지 1", "수령인 1", "010-1234-1234",
                                "10010", "테스트 1 주소", "테스트 1 상세 주소",
                                "요청사항 1"),
                        new DeliveryResponse(
                                2L, "배송지 2", "수령인 2", "010-2345-2345",
                                "10020", "테스트 1 주소", "테스트 1 상세 주소",
                                "요청사항 1")
                )
        );

        // when & then
        mockMvc.perform(get("/delivery"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("사용자가 배송지 정보를 추가한다.")
    void add_delivery() throws Exception {
        // given
        DeliveryRequest deliveryRequest = new DeliveryRequest(
                "배송지 1", "수령인 1", "010-1234-1234", "01232",
                "테스트 주소 1", "테스트 상세주소 1", "테스트 요청사항 1"
        );
        String content = objectMapper.writeValueAsString(deliveryRequest);

        when(deliveryService.create(any(), any())).thenReturn(
                new DeliveryResponse(
                        1L, "배송지 1", "수령인 1", "010-1234-1234",
                        "10010", "테스트 1 주소", "테스트 1 상세 주소",
                        "요청사항 1")
        );

        // when & then
        mockMvc.perform(post("/delivery")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/delivery/1"))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("사용자가 자신이 등록한 배송지 정보를 삭제한다.")
    void delete_delivery() throws Exception {
        // given

        // when & then
        mockMvc.perform(delete("/delivery/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}