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
    private String corporateRegistrationNumber;
    private String telNo;
    private String address;
    private String loginId;
    private String password;
    private boolean isApproved;
    private LocalDateTime createdAt;

    public PartnerDto(String name, String ceoName, String corporateRegistrationNumber, String telNo, String address,
                      String loginId, String password) {
        this.name = name;
        this.ceoName = ceoName;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.telNo = telNo;
        this.address = address;
        this.loginId = loginId;
        this.password = password;
    }

    public static PartnerDto of(Partner partner) {
        return new PartnerDto(partner.getId(), partner.getName(), partner.getCeoName(),
                partner.getCorporateRegistrationNumber(), partner.getTelNo(), partner.getAddress(),
                partner.getLoginId(), partner.getPassword(), partner.isApproved(), partner.getCreatedAt());
    }

    public static Partner to(PartnerDto dto) {
        return Partner.builder()
                .id(dto.getId())
                .name(dto.getName())
                .ceoName(dto.getCeoName())
                .corporateRegistrationNumber(dto.getCorporateRegistrationNumber())
                .telNo(dto.getTelNo())
                .address(dto.getAddress())
                .loginId(dto.getLoginId())
                .password(dto.getPassword())
                .build();
    }
}
