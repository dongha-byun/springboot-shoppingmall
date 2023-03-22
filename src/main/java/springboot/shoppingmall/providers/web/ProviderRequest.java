package springboot.shoppingmall.providers.web;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.providers.dto.ProviderDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderRequest {

    @NotBlank(message = "사업체 이름은 필수항목 입니다.")
    private String name;

    private String ceoName;

    @NotBlank(message = "사업자번호는 필수항목 입니다.")
    private String corporateRegistrationNumber;

    @NotBlank(message = "대표번호는 필수항목 입니다.")
    private String telNo;

    @NotBlank(message = "사업장 주소는 필수항목 입니다.")
    private String address;
    private String loginId;
    private String password;
    private String confirmPassword;

    public static ProviderDto toDto(ProviderRequest request) {
        return new ProviderDto(
                request.getName(), request.getCeoName(), request.getCorporateRegistrationNumber(),
                request.getTelNo(), request.getAddress(), request.getLoginId(), request.getPassword()
        );
    }
}
