package springboot.shoppingmall.partners.application.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.partners.domain.Partner;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartnerRegisterRequestDto {
    private String name;
    private String ceoName;
    private String address;
    private String telNo;
    private String crn;
    private String email;
    private String password;
    private String confirmPassword;

    public Partner toEntity() {
        return Partner.create(
                name, ceoName, address, telNo, crn, email, password
        );
    }
}
