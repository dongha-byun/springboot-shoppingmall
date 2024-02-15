package springboot.shoppingmall.partners.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.partners.application.request.PartnerRegisterRequestDto;
import springboot.shoppingmall.partners.dto.PartnerDto;

@Transactional
@SpringBootTest
class PartnerServiceTest {

    @Autowired
    PartnerService partnerService;

    @Test
    @DisplayName("판매 자격을 신청한다.")
    void register() {
        // given
        PartnerRegisterRequestDto requestDto = new PartnerRegisterRequestDto(
                "부실건설", "김부실", "대충시 부실구 순살동", "02-4433-1222", "110-44-66666",
                "busil@architecture.com", "busil1!", "busil1!"
        );

        // when
        Long id = partnerService.register(requestDto);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("판매 자격 신청 시, 비밀번호를 일치시켜야 한다.")
    void register_fail_with_not_equal_password() {
        // given
        PartnerRegisterRequestDto requestDto = new PartnerRegisterRequestDto(
                "부실건설", "김부실", "대충시 부실구 순살동", "02-4433-1222", "110-44-66666",
                "busil@architecture.com", "busil1!", "busil2@"
        );

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> partnerService.register(requestDto)
        );
    }
}