package springboot.shoppingmall.authorization.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.authorization.JwtTokenProvider;
import springboot.shoppingmall.authorization.domain.RefreshToken;
import springboot.shoppingmall.authorization.domain.RefreshTokenRepository;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.authorization.dto.LoginRequest;

@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("로그인 인증 토근 생성")
    void authorizationSuccessTest(){
        // given
        userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222"));

        // when
        TokenResponse tokenResponse = authService.login(new LoginRequest("test", "test1!"), "127.0.0.1");

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
    }

    @Test
    @DisplayName("로그아웃으로 인한 refresh token 삭제 테스트")
    void logoutTest(){
        // given
        User saveUser = userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222"));
        authService.login(new LoginRequest("test", "test1!"), "127.0.0.1");

        // when
        TokenResponse expireToken = authService.logout(saveUser.getId());

        // then
        assertThat(jwtTokenProvider.validateExpireToken(expireToken.getAccessToken()))
                .isFalse();
    }

    @Test
    @DisplayName("access token 재발행 테스트")
    void reCreateAccessTokenTest(){
        // given
        User saveUser = userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222"));
        // 1. 만료된 access token
        String accessToken = jwtTokenProvider.createAccessToken(saveUser, "127.0.0.1");
        // 2. 유효한 refresh token

        String refreshToken = jwtTokenProvider.createRefreshToken(saveUser, "127.0.0.1");
        refreshTokenRepository.save(new RefreshToken(saveUser, refreshToken));

        // when
        TokenResponse token = authService.reCreateAccessToken(accessToken, "127.0.0.1");

        // then
        assertThat(jwtTokenProvider.validateExpireToken(token.getAccessToken()))
                .isTrue();
    }
}