package springboot.shoppingmall.authorization;

import java.util.Date;
import lombok.Data;

@Data
public class AuthorizationResponse {
    private String token;
    private long expireDate;

    public AuthorizationResponse(String token, long expireDate) {
        this.token = token;
        this.expireDate = expireDate;
    }
}
