package springboot.shoppingmall.authorization.controller;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.authorization.service.EmailAuthorizationInfo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmailAuthorizationResponse {
    private String email;
    private LocalDateTime expireTime;

    public static EmailAuthorizationResponse of(EmailAuthorizationInfo info) {
        return new EmailAuthorizationResponse(info.getEmail(), info.getExpireTime());
    }
}
