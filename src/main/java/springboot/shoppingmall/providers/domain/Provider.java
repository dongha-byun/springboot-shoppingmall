package springboot.shoppingmall.providers.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "providers")
@Entity
public class Provider extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ceoName;
    private String address;
    private String telNo;
    private String corporateRegistrationNumber;
    private String loginId;
    private String password;
    private boolean isApproved;
    private int productSequence;

    public Provider(String name, String ceoName, String address, String telNo, String corporateRegistrationNumber, String loginId,
                    String password) {
        this(name, ceoName, address, telNo, corporateRegistrationNumber, loginId, password, false);
    }
    public Provider(String name, String ceoName, String address, String telNo, String corporateRegistrationNumber, String loginId,
                    String password, boolean isApproved) {
        this(null, name, ceoName, address, telNo, corporateRegistrationNumber, loginId, password, isApproved);
    }

    @Builder
    public Provider(Long id, String name, String ceoName, String address, String telNo, String corporateRegistrationNumber,
                    String loginId,
                    String password, boolean isApproved) {
        this.id = id;
        this.name = name;
        this.ceoName = ceoName;
        this.address = address;
        this.telNo = telNo;
        this.corporateRegistrationNumber = corporateRegistrationNumber;
        this.loginId = loginId;
        this.password = password;
        this.isApproved = isApproved;
        this.productSequence = 0;
    }

    public void approve() {
        this.isApproved = true;
    }

    public void stop() {
        this.isApproved = false;
    }

    public int getLastSequence() {
        return ++this.productSequence;
    }

    public String generateProductCode() {
        return this.corporateRegistrationNumber.replace("-", "")
                + String.format("%06d", getLastSequence());
    }
}
