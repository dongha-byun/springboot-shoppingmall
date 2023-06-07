package springboot.shoppingmall.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import springboot.shoppingmall.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
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

    @Embedded
    private TelNo telNo;

    @Column(name = "login_fail_count")
    private int loginFailCount = 0;

    @Embedded
    private UserGradeInfo userGradeInfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();
    @ColumnDefault("false")
    private boolean isLock = false;

    @Builder
    public User(String userName, String loginId, String password, String telNo) {
        this(userName, loginId, password, telNo, 0);
    }

    @Builder
    public User(String userName, String loginId, String password, String telNo, int loginFailCount) {
        this(userName, loginId, password, telNo, loginFailCount, false);
    }

    @Builder
    public User(String userName, String loginId, String password, String telNo, int loginFailCount, boolean isLock) {
        this.userName = userName;
        this.loginId = loginId;
        this.password = password;
        this.telNo = new TelNo(telNo);
        this.loginFailCount = loginFailCount;
        this.isLock = isLock;
        this.userGradeInfo = new UserGradeInfo(UserGrade.NORMAL, 0, 0);
    }

    public void updateUser(User user) {
        this.telNo = user.getTelNo();
        this.password = user.getPassword();
    }

    public boolean isEqualPassword(String password) {
        return this.password.equals(password);
    }

    public void addDelivery(Delivery delivery){
        this.getDeliveries().add(delivery);
    }

    public void removeDelivery(Delivery delivery){
        this.getDeliveries().remove(delivery);
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }

    public void removePayment(Long paymentId) {
        List<Payment> payments = this.payments.stream()
                .filter(payment -> !payment.getId().equals(paymentId))
                .collect(Collectors.toList());
        this.payments.clear();
        this.payments.addAll(payments);
    }

    public String telNo() {
        return this.telNo.getTelNo();
    }

    public int increaseLoginFailCount() {
        this.loginFailCount++;
        if(this.loginFailCount >= 5) {
            this.isLock = true;
        }
        return this.loginFailCount;
    }

    public boolean isLocked() {
        return this.isLock;
    }

    public Optional<UserGrade> getNextUserGrade() {
        return this.userGradeInfo.nextGrade();
    }
}
