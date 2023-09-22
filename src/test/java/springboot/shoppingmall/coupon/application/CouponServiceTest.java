package springboot.shoppingmall.coupon.application;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import springboot.shoppingmall.coupon.domain.Coupon;
import springboot.shoppingmall.coupon.domain.CouponRepository;

@Transactional
@SpringBootTest
class CouponServiceTest {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    void beforeEach() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    @DisplayName("쿠폰 생성 - 특정 회원등급 이상의 회원들에게 쿠폰을 발급한다.")
    void create_coupon_for_user() throws JsonProcessingException {
        // 단골회원(REGULAR) 등급 이상인 회원들에게
        // 사용기한이 2023-05-28 ~ 2023-07-28 인
        // 할인율 7%의 쿠폰을 발급해준다.
        // given
        String name = "기념 할인 쿠폰";
        LocalDateTime fromDate = LocalDateTime.of(2023, 5, 28, 0, 0, 0);
        LocalDateTime toDate = LocalDateTime.of(2023, 7, 28, 23, 59, 59);
        int discountRate = 5;
        Long partnersId = 1L;
        CouponCreateDto couponCreateDto = new CouponCreateDto(
                name, fromDate, toDate, "REGULAR", discountRate, partnersId
        );

        String resultContent = objectMapper.writeValueAsString(Arrays.asList(10L, 20L, 30L));
        mockRestServiceServerWithGetUsersAboveTargetGrade(couponCreateDto.getGrade(), resultContent);

        // when
        Long couponId = couponService.create(couponCreateDto);

        // then
        Coupon savedCoupon = couponRepository.findById(couponId).orElseThrow();
        assertThat(savedCoupon).isNotNull();
        assertThat(savedCoupon.getId()).isEqualTo(couponId);
        assertThat(savedCoupon.getUserCoupons()).hasSize(3);
    }

    private void mockRestServiceServerWithGetUsersAboveTargetGrade(String targetGrade, String resultContent) {
        mockRestServiceServer
                .expect(
                        requestTo("/users/aboveGrade?targetGrade="+targetGrade)
                )
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(resultContent)
                );
    }
}