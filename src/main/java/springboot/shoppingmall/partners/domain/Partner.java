package springboot.shoppingmall.partners.domain;

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
@Table(name = "partners")
@Entity
public class Partner extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ceoName;
    private String address;
    private String telNo;
    private String crn;
    private String email;
    private String password;
    private boolean isApproved;
    private int productSequence;

    public static Partner create(String name, String ceoName, String address, String telNo,
                                 String crn, String email, String password) {
        return new Partner(name, ceoName, address, telNo, crn, email, password);
    }

    public Partner(String name, String ceoName, String address, String telNo,
                   String crn, String email, String password) {
        this(name, ceoName, address, telNo, crn, email, password, false);
    }
    public Partner(String name, String ceoName, String address, String telNo,
                   String crn, String email, String password, boolean isApproved) {
        this(null, name, ceoName, address, telNo, crn, email, password, isApproved);
    }

    @Builder
    public Partner(Long id, String name, String ceoName, String address, String telNo, String crn,
                   String email, String password, boolean isApproved) {
        this.id = id;
        this.name = name;
        this.ceoName = ceoName;
        this.address = address;
        this.telNo = telNo;
        this.crn = crn;
        this.email = email;
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
        return this.crn.replace("-", "")
                + String.format("%06d", getLastSequence());
    }
}
