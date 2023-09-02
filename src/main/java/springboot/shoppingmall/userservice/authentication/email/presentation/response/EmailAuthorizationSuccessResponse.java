package springboot.shoppingmall.userservice.authentication.email.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.authentication.email.application.dto.EmailAuthorizationInfo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmailAuthorizationSuccessResponse {
    private String email;
    private String message;

    public static EmailAuthorizationSuccessResponse of(EmailAuthorizationInfo info) {
        return new EmailAuthorizationSuccessResponse(info.getEmail(), info.getMessage());
    }
}
