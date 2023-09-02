package springboot.shoppingmall.userservice.authentication.email.presentation.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.authentication.email.application.dto.EmailAuthenticationInfo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmailAuthenticationResponse {
    private String email;
    private LocalDateTime expireTime;

    public static EmailAuthenticationResponse of(EmailAuthenticationInfo info) {
        return new EmailAuthenticationResponse(info.getEmail(), info.getExpireTime());
    }
}
