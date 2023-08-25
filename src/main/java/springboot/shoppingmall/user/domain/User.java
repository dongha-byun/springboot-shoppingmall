package springboot.shoppingmall.user.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="email", column = @Column(name = "email")),
            @AttributeOverride(name="password", column = @Column(name = "password")),
            @AttributeOverride(name="loginFailCount", column = @Column(name = "login_fail_count")),

    })
    private LoginInfo loginInfo;

    private LocalDateTime signUpDate;

    @Embedded
    private TelNo telNo;

    @Embedded
    private UserGradeInfo userGradeInfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();
    @ColumnDefault("false")
    private boolean isLock = false;

    public User(String userName, String email, String password, String telNo) {
        this(userName, email, password, telNo, LocalDateTime.now());
    }

    @Builder
    public User(String userName, String email, String password, String telNo, LocalDateTime signUpDate) {
        this(userName, email, password, telNo, signUpDate, 0);
    }

    public User(String userName, String email, String password, String telNo, LocalDateTime signUpDate,
                int loginFailCount) {
        this(userName, email, password, telNo, signUpDate, loginFailCount, false);
    }

    public User(String userName, String email, String password, String telNo, LocalDateTime signUpDate,
                int loginFailCount, boolean isLock) {
        this(userName, email, password, telNo, signUpDate, loginFailCount, isLock,
                new UserGradeInfo(UserGrade.NORMAL, 0, 0));
    }

    public User(String userName, String email, String password, String telNo, LocalDateTime signUpDate,
                int loginFailCount, boolean isLock, UserGradeInfo userGradeInfo) {
        this.userName = userName;
        this.loginInfo = new LoginInfo(email, password, loginFailCount);
        this.telNo = new TelNo(telNo);
        this.signUpDate = signUpDate;
        this.isLock = isLock;
        this.userGradeInfo = userGradeInfo;
    }


    public void updateUser(String telNo, String password) {
        this.telNo = new TelNo(telNo);
        this.loginInfo = this.loginInfo.changePassword(password);
    }

    public boolean isEqualPassword(String password) {
        return this.loginInfo.checkPasswordEqual(password);
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
        int afterIncreaseFailCount = this.loginInfo.increaseLoginFailCount();
        if(afterIncreaseFailCount >= 5) {
            this.isLock = true;
        }
        return afterIncreaseFailCount;
    }

    public boolean isLocked() {
        return this.isLock;
    }

    public Optional<UserGrade> getNextUserGrade() {
        return this.userGradeInfo.nextGrade();
    }

    public void increaseOrderAmount(int amounts) {
        this.userGradeInfo.increaseOrderAmount(amounts);
    }

    public int discountRate() {
        return this.userGradeInfo.getGrade().getDiscountRate();
    }

    public String getPassword() {
        return this.loginInfo.getPassword();
    }

    public String getEmail() {
        return this.loginInfo.getEmail();
    }

    public int getLoginFailCount() {
        return this.loginInfo.getLoginFailCount();
    }
}
