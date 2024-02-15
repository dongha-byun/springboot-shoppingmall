package springboot.shoppingmall.partners.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.partners.dto.PartnerDto;
import springboot.shoppingmall.utils.DateUtils;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PartnerResponse {
    private Long id;
    private String name;
    private String ceoName;
    private String crn;
    private String telNo;
    private String address;
    private String email;
    private boolean isApproved;
    private String createdAt;

    public static PartnerResponse of(PartnerDto dto) {
        return PartnerResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .ceoName(dto.getCeoName())
                .crn(dto.getCrn())
                .telNo(dto.getTelNo())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .isApproved(dto.isApproved())
                .createdAt(DateUtils.toStringOfLocalDateTIme(dto.getCreatedAt()))
                .build();
    }
}
