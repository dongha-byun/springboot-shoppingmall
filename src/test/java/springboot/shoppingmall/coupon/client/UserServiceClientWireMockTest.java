package springboot.shoppingmall.coupon.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import springboot.shoppingmall.client.userservice.UserServiceClient;
import springboot.shoppingmall.client.userservice.response.ResponseUserInformation;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ContextConfiguration(classes = {TestWireMockConfig.class})
public class UserServiceClientWireMockTest {

    @Autowired
    UserServiceClient service;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WireMockServer mockUserMicroService;

    @Test
    @DisplayName("쿠폰을 발급받은 사용자 목록을 조회한다.")
    void get_users_has_coupon() throws JsonProcessingException {
        // given
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);
        String requestBody = objectMapper.writeValueAsString(userIds);

        List<ResponseUserInformation> users = Arrays.asList(
                new ResponseUserInformation(1L, "사용자1", "일반회원"),
                new ResponseUserInformation(2L, "사용자2", "VIP"),
                new ResponseUserInformation(3L, "사용자3", "단골회원")
        );
        String responseBody = objectMapper.writeValueAsString(users);

        mockUserMicroService
                .stubFor(post(urlEqualTo("/users/has-coupon"))
                        .withRequestBody(equalToJson(requestBody))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)));

        // when
        List<ResponseUserInformation> result = service.getUsers(userIds);

        // then
        assertThat(result)
                .hasSize(3)
                .extracting("userId", "userName", "grade")
                .containsExactly(
                        tuple(1L, "사용자1", "일반회원"),
                        tuple(2L, "사용자2", "VIP"),
                        tuple(3L, "사용자3", "단골회원")
                );
    }

    @Test
    @DisplayName("쿠폰 발급 대상을 위해 특정 회원등급 이상인 회원들을 조회한다.")
    void get_users_above_grade() throws JsonProcessingException {
        // given
        List<Long> userIds = Arrays.asList(1L, 2L, 3L);
        String responseBody = objectMapper.writeValueAsString(userIds);
        mockUserMicroService
                .stubFor(get(urlEqualTo("/users/above-grade?targetGrade=REGULAR"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBody)));

        // when
        List<Long> result = service.getUserIdsAboveTheGrade("REGULAR");

        // then
        assertThat(result)
                .hasSize(3)
                .containsExactly(1L, 2L, 3L);
    }
}
