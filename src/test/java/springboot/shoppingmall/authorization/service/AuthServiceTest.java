package springboot.shoppingmall.authorization.service;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.authorization.TestJwtTokenExpireDurationStrategy;
import springboot.shoppingmall.authorization.domain.RefreshTokenRepository;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.authorization.exception.TryLoginLockedUserException;
import springboot.shoppingmall.authorization.exception.WrongPasswordException;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class AuthServiceTest {

    AuthService authService;
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    EntityManager em;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserFinder userFinder;

    @BeforeEach
    void setUp(){
        jwtTokenProvider = new JwtTokenProvider(new TestJwtTokenExpireDurationStrategy());
        authService = new AuthService(jwtTokenProvider, userFinder, refreshTokenRepository);
    }

    @Test
    @DisplayName("로그인 테스트")
    void login_test() throws WrongPasswordException {
        // given
        userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222"));

        // when
        TokenResponse tokenResponse = authService.login("test", "test1!", "127.0.0.1");

        // then
        assertThat(tokenResponse.getAccessToken()).isNotNull();
        assertThat(tokenResponse.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("로그아웃으로 인한 refresh token 삭제 테스트")
    void logoutTest() throws WrongPasswordException {
        // given
        User saveUser = userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222"));
        authService.login("test", "test1!", "127.0.0.1");

        // when
        TokenResponse expireToken = authService.logout(saveUser.getId());

        // then
        assertThat(jwtTokenProvider.validateExpireToken(expireToken.getAccessToken()))
                .isFalse();
    }

    @Test
    @DisplayName("access token 재발행 테스트")
    void reCreateAccessTokenTest() throws WrongPasswordException {
        // given
        userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222"));
        TokenResponse tokenResponse = authService.login("test", "test1!", "127.0.0.1");

        // when
        TokenResponse token = authService.reCreateAccessToken(tokenResponse.getAccessToken(), "127.0.0.1");

        // then
        assertThat(jwtTokenProvider.validateExpireToken(token.getAccessToken()))
                .isTrue();
    }

    @Test
    @DisplayName("로그인된 사용자의 인가 처리")
    void authorize_test() throws WrongPasswordException {
        // given
        User user = userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222"));
        TokenResponse tokenResponse = authService.login("test", "test1!", "127.0.0.1");

        // when
        AuthorizedUser authorizedUser = authService.getAuthorizedUser(tokenResponse.getAccessToken(), "127.0.0.1");

        // then
        assertThat(authorizedUser.getId()).isEqualTo(user.getId());
        assertThat(authorizedUser.getLoginId()).isEqualTo(user.getLoginId());
    }

    @Test
    @DisplayName("로그인 실패 시, 실패 횟수 증가")
    void login_fail_count_increment_test() {
        // given
        User user = userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222"));

        // when
        로그인("test", "test2!", "127.0.0.1");

        // then
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(findUser.getLoginFailCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("로그인 실패가 5회 이상이면, 계정이 잠금상태가 된다.")
    void cant_login_by_login_fail_count() {
        // given
        User user = userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222", 4));
        String loginId = "test";
        String password = "test2!";
        String localhost = "127.0.0.1";

        // when
        로그인(loginId, password, localhost);

        // then
        User findUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(findUser.isLocked()).isTrue();
    }

    @Test
    @DisplayName("계정이 잠긴채로 로그인을 시도하면, 로그인이 불가하다.")
    void cant_login_by_user_locked() {
        // given
        User user = userRepository.save(new User("테스터", "test", "test1!", "010-1111-2222", 5, true));
        String loginId = "test";
        String password = "test1!";
        String localhost = "127.0.0.1";

        // when & then
        잠금_계정_로그인_검증(loginId, password, localhost);
    }

    private void 로그인(String loginId, String password, String localhost) {
        assertThatThrownBy(
                () -> authService.login(loginId, password, localhost)
        ).isInstanceOf(WrongPasswordException.class);
    }

    private void 잠금_계정_로그인_검증(String loginId, String password, String localhost) {
        assertThatThrownBy(
                () -> authService.login(loginId, password, localhost)
        ).isInstanceOf(TryLoginLockedUserException.class);
    }
}