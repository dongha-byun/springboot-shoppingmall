package springboot.shoppingmall.providers.web;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.providers.dto.ProviderDto;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProviderResponse {
    private Long id;
    private String loginId;
    private LocalDateTime createdAt;
    private boolean isApproved;

    public static ProviderResponse of(ProviderDto dto) {
        return new ProviderResponse(dto.getId(), dto.getLoginId(), dto.getCreatedAt(), dto.isApproved());
    }
}
