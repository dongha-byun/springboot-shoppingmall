package springboot.shoppingmall.userservice.authentication.email.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import springboot.shoppingmall.TestEmailAuthenticationConfig;
import springboot.shoppingmall.authorization.service.AuthService;
import springboot.shoppingmall.authorization.service.JwtTokenProvider;
import springboot.shoppingmall.userservice.authentication.email.application.EmailAuthenticationService;
import springboot.shoppingmall.userservice.authentication.email.application.dto.EmailAuthenticationInfo;
import springboot.shoppingmall.userservice.authentication.email.presentation.request.AuthenticationMailRequest;
import springboot.shoppingmall.userservice.authentication.email.presentation.request.AuthenticationRequest;

@Import({TestEmailAuthenticationConfig.class})
@WebMvcTest(EmailAuthenticationController.class)
class EmailAuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    EmailAuthenticationService emailAuthenticationService;


    @Test
    @DisplayName("이메일로 인증번호를 발송하면, 인증확인 화면으로 이동시킨다.")
    void view_check_auth_after_send_auth_code_email() throws Exception {
        // given
        AuthenticationMailRequest authorizationMailRequest = new AuthenticationMailRequest("test@test.com");
        String content = objectMapper.writeValueAsString(authorizationMailRequest);

        when(emailAuthenticationService.createCode(any(), any())).thenReturn(
                new EmailAuthenticationInfo(
                        "test@test.com",
                        LocalDateTime.of(2023, 8, 11, 12, 0, 0),
                        "인증이 완료되었습니다."
                )
        );

        // when & then
        mockMvc.perform(post("/send-authorize-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("http://localhost:3000/authorized-code-form"))
                .andExpect(jsonPath("message", is("인증이 완료되었습니다.")));
    }

    @Test
    @DisplayName("인증번호가 맞지 않으면, 재입력을 요구한다.")
    void not_collect_auth_code() throws Exception {
        // given
        AuthenticationRequest authorizationRequest = new AuthenticationRequest("test@test.com", "123456");
        String content = objectMapper.writeValueAsString(authorizationRequest);

        when(emailAuthenticationService.checkCode(any(), any(), any())).thenThrow(
                new IllegalArgumentException("인증번호가 맞지 않습니다.")
        );

        // when & then
        mockMvc.perform(post("/check-authorized-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", is("인증번호가 맞지 않습니다.")));
    }
}