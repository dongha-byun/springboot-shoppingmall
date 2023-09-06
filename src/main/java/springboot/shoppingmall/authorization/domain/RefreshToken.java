package springboot.shoppingmall.authorization.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.BaseEntity;
import springboot.shoppingmall.userservice.user.domain.User;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String refreshToken;

    @Builder
    public RefreshToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }
}
