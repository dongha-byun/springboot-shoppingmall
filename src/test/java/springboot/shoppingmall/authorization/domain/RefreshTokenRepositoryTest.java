package springboot.shoppingmall.authorization.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @DisplayName("user 의 refresh token 조회")
    void findByUserTest(){
        // given
        User saveUser = userRepository.save(
                User.builder()
                        .userName("테스터")
                        .loginId("tester")
                        .password("tester1!")
                        .telNo("010-1234-1234")
                        .build()
        );
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(saveUser)
                        .refreshToken("refreshToken")
                        .build()
        );

        // when
        RefreshToken refreshToken = refreshTokenRepository.findByUser(saveUser)
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(refreshToken.getId()).isNotNull();
    }

}