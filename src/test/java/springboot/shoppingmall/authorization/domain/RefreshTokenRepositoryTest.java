package springboot.shoppingmall.authorization.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserRepository;

@Transactional
@SpringBootTest
class RefreshTokenRepositoryTest {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    User saveUser;

    @BeforeEach
    void beforeEach(){
        saveUser = userRepository.save(
                User.builder()
                        .userName("테스터")
                        .email("tester@test.com")
                        .password("tester1!")
                        .telNo("010-1234-1234")
                        .build()
        );
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userId(saveUser.getId())
                        .refreshToken("refreshToken")
                        .build()
        );
    }

    @Test
    @DisplayName("user 의 refresh token 조회")
    void findByUserTest(){
        // given

        // when
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(saveUser.getId())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(refreshToken.getId()).isNotNull();
    }

    @Test
    @DisplayName("user 의 refresh token 삭제")
    void deleteByUserTest(){
        // given

        // when
        refreshTokenRepository.deleteByUserId(saveUser.getId());

        // then
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserId(saveUser.getId());
        assertThat(refreshToken.isPresent()).isFalse();
    }

}