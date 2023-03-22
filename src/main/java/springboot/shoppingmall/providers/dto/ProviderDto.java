package springboot.shoppingmall.providers.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.providers.domain.Provider;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderDto {
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

    public ProviderDto(String name, String ceoName, String corporateRegistrationNumber, String telNo, String address,
                       String loginId, String password) {
        this.name = name;
        this.ceoName = ceoName;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.telNo = telNo;
        this.address = address;
        this.loginId = loginId;
        this.password = password;
    }

    public static ProviderDto of(Provider provider) {
        return new ProviderDto(provider.getId(), provider.getName(), provider.getCeoName(),
                provider.getCorporateRegistrationNumber(), provider.getTelNo(), provider.getAddress(),
                provider.getLoginId(), provider.getPassword(), provider.isApproved(), provider.getCreatedAt());
    }

    public static Provider to(ProviderDto dto) {
        return Provider.builder()
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
