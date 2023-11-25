package springboot.shoppingmall.partners.presentation;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.partners.application.PartnerLoginService;
import springboot.shoppingmall.partners.presentation.request.PartnerLoginRequest;

@WebMvcTest(controllers = PartnersLoginController.class)
class PartnersLoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PartnerLoginService partnerLoginService;

    @Test
    @DisplayName("판매자 로그인을 성공한다.")
    void partner_login_success() throws Exception {
        // given
        PartnerLoginRequest partnerLoginRequest = new PartnerLoginRequest(
                "partnerLoginId", "partnerLoginPassword"
        );
        String data = objectMapper.writeValueAsString(partnerLoginRequest);
        when(partnerLoginService.login(any(), any())).thenReturn(
                "Partner-Access-Token"
        );

        // when & then
        mockMvc.perform(post("/partners/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("판매자 로그인 시, 아이디/패스워드는 필수이다.")
    void partner_login_fail() throws Exception {
        // given
        PartnerLoginRequest partnerLoginRequest = new PartnerLoginRequest();
        String data = objectMapper.writeValueAsString(partnerLoginRequest);

        // when & then
        mockMvc.perform(post("/partners/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder(
                        "아이디를 입력하세요.",
                        "패스워드를 입력하세요."
                )));
    }
}