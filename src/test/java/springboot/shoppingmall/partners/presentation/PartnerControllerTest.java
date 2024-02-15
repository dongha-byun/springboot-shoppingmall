package springboot.shoppingmall.partners.presentation;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.partners.application.PartnerService;
import springboot.shoppingmall.partners.presentation.request.PartnerRegisterRequest;

@WebMvcTest(controllers = PartnerController.class)
class PartnerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PartnerService partnerService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("판매 자격을 신청한다.")
    void register_partner() throws Exception {
        // given
        PartnerRegisterRequest request = new PartnerRegisterRequest(
                "부실건설", "김부실", "1102233311", "010-2344-1122", "서울시 대충구 부실동",
                "busil@test.com", "busil1!", "busil1!"
        );
        String requestBody = objectMapper.writeValueAsString(request);

        when(partnerService.register(any())).thenReturn(1L);

        // when & then
        mockMvc.perform(post("/partners/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("판매 자격신청이 완료되었습니다.")));
    }

    @Test
    @DisplayName("필수항목 값이 입력되지 않으면, 판매 자격신청이 불가능하다.")
    void register_fail_with_blank_parameter() throws Exception {
        // given
        PartnerRegisterRequest request = new PartnerRegisterRequest(
                "", "", "", "", "",
                "", "", ""
        );
        String requestBody = objectMapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/partners/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(8)))
                .andExpect(jsonPath("$.errors[*].message", hasItems(
                        "사업체 이름은 필수항목 입니다.",
                        "대표자 명은 필수항목 입니다.",
                        "사업자번호는 필수항목 입니다.",
                        "대표번호는 필수항목 입니다.",
                        "사업장 주소는 필수항목 입니다.",
                        "회사 대표 이메일은 필수항목 입니다.",
                        "비밀번호는 필수항목 입니다.",
                        "비밀번호 확인은 필수입니다."
                )));
    }
}