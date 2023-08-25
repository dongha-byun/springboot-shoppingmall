package springboot.shoppingmall.authorization.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.authorization.service.EmailAuthorizationInfo;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EmailAuthorizationSuccessResponse {
    private String email;

    public static EmailAuthorizationSuccessResponse of(EmailAuthorizationInfo info) {
        return new EmailAuthorizationSuccessResponse(info.getEmail());
    }
}
