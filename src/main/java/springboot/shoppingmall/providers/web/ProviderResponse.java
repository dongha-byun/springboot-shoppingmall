package springboot.shoppingmall.providers.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.providers.dto.ProviderDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderResponse {
    private Long id;
    private String name;
    private String ceoName;
    private String corporateRegistrationNumber;
    private String telNo;
    private String loginId;
    private String createdAt;
    private boolean isApproved;

    public static ProviderResponse of(ProviderDto dto) {
        return new ProviderResponse(dto.getId(), dto.getName(), dto.getCeoName(), dto.getCorporateRegistrationNumber(),
                dto.getTelNo(), dto.getLoginId(), dateFormatting(dto.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"), dto.isApproved());
    }

    private static String dateFormatting(LocalDateTime time, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return time.format(dateTimeFormatter);
    }
}
