package springboot.shoppingmall.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import springboot.shoppingmall.BaseEntity;
import springboot.shoppingmall.user.dto.UserRequest;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String userName;

    @Column(length = 30, unique = true, nullable = false)
    private String loginId;

    @Column(length = 400, nullable = false)
    private String password;

    @Column(nullable = false)
    private String telNo;

    protected User(){

    }

    public User(String userName, String loginId, String password, String telNo) {
        this.userName = userName;
        this.loginId = loginId;
        this.password = password;
        this.telNo = telNo;
    }


    public void updateUser(UserRequest userRequest) {
        this.telNo = userRequest.getTelNo();
        this.password = userRequest.getPassword();
    }

    public boolean isEqualPassword(String password) {
        return this.password.equals(password);
    }
}
