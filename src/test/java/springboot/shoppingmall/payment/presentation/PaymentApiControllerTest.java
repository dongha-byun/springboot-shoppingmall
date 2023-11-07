package springboot.shoppingmall.payment.presentation;

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
import springboot.shoppingmall.payment.application.PaymentService;
import springboot.shoppingmall.payment.application.dto.PaymentDto;
import springboot.shoppingmall.payment.domain.CardCompany;
import springboot.shoppingmall.payment.domain.PayType;
import springboot.shoppingmall.payment.presentation.request.PaymentRequest;
import springboot.shoppingmall.providers.config.PartnersConfiguration;

@WebMvcTest(
        controllers = PaymentApiController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {
                PartnersConfiguration.class
        })
)
class PaymentApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PaymentService paymentService;

    @Test
    @DisplayName("사용자가 결제수단을 추가한다.")
    void create_payment() throws Exception {
        // given
        PaymentRequest paymentRequest =
                new PaymentRequest("0011", "1122", "1234", "4455",
                        "08", "26", "465", "CARD", "SS");
        String content = objectMapper.writeValueAsString(paymentRequest);

        when(paymentService.createPayment(any(), any())).thenReturn(
                new PaymentDto(10L, PayType.CARD, CardCompany.SS,
                        "0011", "1122", "1234", "4455",
                        "08", "26", "465")
        );

        // when & then
        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("사용자가 결제수단 목록을 조회한다.")
    void find_all_payment() throws Exception {
        // given
        when(paymentService.findAllPayments(any())).thenReturn(
                Arrays.asList(
                        new PaymentDto(10L, PayType.CARD, CardCompany.SS,
                                "0011", "1122", "1234", "4455",
                                "08", "26", "465"),
                        new PaymentDto(20L, PayType.KAKAO_PAY, CardCompany.SS,
                                "", "", "", "", "", "", ""),
                        new PaymentDto(30L, PayType.CARD, CardCompany.SH,
                                "1122", "3333", "4313", "1444",
                                "12", "30", "122")
                )
        );

        // when & then
        mockMvc.perform(get("/payments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("사용자가 결제수단 목록을 조회한다.")
    void delete_payment() throws Exception {
        // given

        // when & then
        mockMvc.perform(delete("/payments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}