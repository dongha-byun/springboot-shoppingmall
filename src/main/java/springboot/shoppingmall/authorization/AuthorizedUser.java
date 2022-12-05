package springboot.shoppingmall.authorization;

import lombok.Data;

@Data
public class AuthorizedUser {
    private Long id;

    public AuthorizedUser() {
    }

    public AuthorizedUser(Long id) {
        this.id = id;
    }
}
