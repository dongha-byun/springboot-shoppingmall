package springboot.shoppingmall.authorization;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.shoppingmall.authorization.service.JwtTokenProvider;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void beforeEach(){
        user = userRepository.save(User.builder()
                .userName("테스터1").loginId("test1").password("test1!").telNo("010-0000-0000")
                .build());
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
    
}