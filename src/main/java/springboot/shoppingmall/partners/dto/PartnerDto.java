package springboot.shoppingmall.partners.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.partners.domain.Partner;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnerDto {
    private Long id;
    private String name;
    private String ceoName;
    private String crn;
    private String telNo;
    private String address;
    private String email;
    private String password;
    private boolean isApproved;
    private LocalDateTime createdAt;

    public PartnerDto(String name, String ceoName, String crn, String telNo, String address,
                      String email, String password) {
        this.name = name;
        this.ceoName = ceoName;
        this.crn = crn;
        this.telNo = telNo;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    public static PartnerDto of(Partner partner) {
        return new PartnerDto(partner.getId(), partner.getName(), partner.getCeoName(),
                partner.getCrn(), partner.getTelNo(), partner.getAddress(),
                partner.getEmail(), partner.getPassword(), partner.isApproved(), partner.getCreatedAt());
    }

}
