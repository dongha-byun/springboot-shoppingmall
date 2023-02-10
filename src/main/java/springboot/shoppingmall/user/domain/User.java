package springboot.shoppingmall.user.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import springboot.shoppingmall.BaseEntity;
import springboot.shoppingmall.user.dto.UserRequest;

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

    @Column(nullable = false)
    private String telNo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Basket> baskets = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveries = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @Builder
    public User(String userName, String loginId, String password, String telNo) {
        this.userName = userName;
        this.loginId = loginId;
        this.password = password;
        this.telNo = telNo;
    }

    public void updateUser(User user) {
        this.telNo = user.getTelNo();
        this.password = user.getPassword();
    }

    public boolean isEqualPassword(String password) {
        return this.password.equals(password);
    }

    public void addBasket(Basket basket){
        this.getBaskets().add(basket);
    }

    public void removeBasket(Basket basket) {
        this.getBaskets().remove(basket);
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
}
