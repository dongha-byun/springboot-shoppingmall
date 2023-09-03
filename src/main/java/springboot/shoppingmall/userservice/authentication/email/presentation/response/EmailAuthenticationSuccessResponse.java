package springboot.shoppingmall.userservice.authentication.email.presentation.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.authentication.email.application.dto.EmailAuthenticationInfo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmailAuthenticationSuccessResponse {
    private String email;
    private LocalDateTime expireTime;
    private String message;

    public static EmailAuthenticationSuccessResponse of(EmailAuthenticationInfo info) {
        return new EmailAuthenticationSuccessResponse(info.getEmail(), info.getExpireTime() ,info.getMessage());
    }
}
