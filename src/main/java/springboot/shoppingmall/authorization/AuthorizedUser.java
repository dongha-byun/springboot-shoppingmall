package springboot.shoppingmall.authorization;

import lombok.Data;

@Data
public class AuthorizedUser {
    private Long id;
    private String loginId;

    public AuthorizedUser() {
    }

    public AuthorizedUser(Long id) {
        this.id = id;
    }

    public AuthorizedUser(Long id, String loginId) {
        this.id = id;
        this.loginId = loginId;
    }
}
