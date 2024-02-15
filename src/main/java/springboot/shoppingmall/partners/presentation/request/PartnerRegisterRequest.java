package springboot.shoppingmall.partners.presentation.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;
import springboot.shoppingmall.partners.application.request.PartnerRegisterRequestDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartnerRegisterRequest {

    @NotBlank(message = "사업체 이름은 필수항목 입니다.")
    private String name;

    @NotBlank(message = "대표자 명은 필수항목 입니다.")
    private String ceoName;

    @NotBlank(message = "사업자번호는 필수항목 입니다.")
    private String crn;

    @NotBlank(message = "대표번호는 필수항목 입니다.")
    private String telNo;

    @NotBlank(message = "사업장 주소는 필수항목 입니다.")
    private String address;

    @NotBlank(message = "회사 대표 이메일은 필수항목 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수항목 입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

    public PartnerRegisterRequestDto toDto() {
        return new PartnerRegisterRequestDto(
                name, ceoName, address, telNo, crn, email, password, confirmPassword
        );
    }
}
