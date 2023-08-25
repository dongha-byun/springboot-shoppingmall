package springboot.shoppingmall.user.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class LoginInfo {
    @Column(length = 30, unique = true, nullable = false)
    private String email;

    @Column(length = 400, nullable = false)
    private String password;

    @Column(name = "login_fail_count")
    private int loginFailCount = 0;

    public LoginInfo(String email, String password, int loginFailCount) {
        this.email = email;
        this.password = password;
        this.loginFailCount = loginFailCount;
    }

    public boolean checkPasswordEqual(String password) {
        return this.password.equals(password);
    }

    public int increaseLoginFailCount() {
        return ++ this.loginFailCount;
    }

    public LoginInfo changePassword(String password) {
        return new LoginInfo(this.email, password, 0);
    }
}
