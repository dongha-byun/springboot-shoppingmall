package springboot.shoppingmall.authorization;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.authorization.exception.ExpireTokenException;
import springboot.shoppingmall.authorization.service.JwtTokenProvider;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class JwtTokenProviderTest {

    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void beforeEach(){
        user = userRepository.save(User.builder()
                .userName("테스터1").loginId("test1").password("test1!").telNo("010-0000-0000")
                .build());

        jwtTokenProvider = new JwtTokenProvider(new TestJwtTokenExpireDurationStrategy());
    }

    @Test
    @DisplayName("토큰 생성 테스트")
    void createTokenTest(){
        // given

        // when
        String accessToken = jwtTokenProvider.createAccessToken(user, "127.0.0.1");

        // then
        assertThat(accessToken).isNotNull();

        Long userId = jwtTokenProvider.getUserId(accessToken);
        assertThat(userId).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("토큰 유효기간 테스트")
    void reCreateTokenTest() throws InterruptedException {
        // 유효기간이 짧은 토큰을 생성하면
        // 토큰이 만료시, 리프레시 토큰에 의해 재발급받는다
        // 재발급 받은게 유효한지 체크한다.

        // given
        String accessToken = jwtTokenProvider.createAccessToken(user, "127.0.0.1");
        Thread.sleep(1000);

        // when & then
        assertThat(jwtTokenProvider.validateExpireToken(accessToken)).isFalse();
    }

    @Test
    @DisplayName("토큰 Ip 비교 테스트")
    void validate_token_by_ip() {
        // given
        String accessToken = jwtTokenProvider.createAccessToken(user, "127.0.0.1");

        // when & then
        assertThat(jwtTokenProvider.validateIpToken(accessToken, "127.0.0.2")).isFalse();
    }
}