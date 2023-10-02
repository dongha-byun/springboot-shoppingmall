package springboot.shoppingmall.providers.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.providers.domain.Provider;
import springboot.shoppingmall.providers.domain.ProviderRepository;
import springboot.shoppingmall.providers.dto.ProviderDto;

@Transactional
@SpringBootTest
class ProviderLoginServiceTest {

    @Autowired
    ProviderLoginService loginService;

    @Autowired
    ProviderRepository providerRepository;

    @Test
    @DisplayName("판매 자격이 승인된 계정의 로그인 성공")
    void login_success() {
        // given
        String loginId = "provider1";
        Provider saveProvider = providerRepository.save(new Provider(
                "판매업체1", "판매자1", "판매시 반품동", "02-1112-3333",
                "111-33-443322", loginId, "1q2w3e4r!"
        ));
        saveProvider.approve();

        // when
        ProviderDto providerDto = loginService.login(loginId, "1q2w3e4r!");

        // then
        assertThat(providerDto.getId()).isNotNull();
        assertThat(providerDto.getLoginId()).isEqualTo("provider1");
    }

    @Test
    @DisplayName("판매 자격이 승인되지 않은 계정은 로그인에 실패한다.")
    void login_fail_with_not_approve() {
        // given
        String loginId = "provider1";
        providerRepository.save(new Provider(
                "판매업체1", "판매자1", "판매시 반품동", "02-1112-3333",
                "111-33-443322", loginId, "1q2w3e4r!"
        ));

        // when & then
        assertThatIllegalStateException().isThrownBy(
                () -> loginService.login(loginId, "1q2w3e4r!")
        );
    }

    @Test
    @DisplayName("판매 자격이 승인되었어도 비밀번호가 틀리면 로그인에 실패한다.")
    void login_fail_with_not_collect_password() {
        // given
        String loginId = "provider1";
        Provider provider = providerRepository.save(new Provider(
                "판매업체1", "판매자1", "판매시 반품동", "02-1112-3333",
                "111-33-443322", loginId, "1q2w3e4r!"
        ));
        provider.approve();

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> loginService.login(loginId, "1q2w3e4r@")
        );
    }
}