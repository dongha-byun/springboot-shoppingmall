package springboot.shoppingmall.user.domain;

import java.util.Optional;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class UserGradeInfo {

    @Enumerated(EnumType.STRING)
    private UserGrade grade;
    private int orderCount;
    private int amount;

    public UserGradeInfo(UserGrade grade, int orderCount, int amount) {
        this.grade = grade;
        this.orderCount = orderCount;
        this.amount = amount;
    }

    public Optional<UserGrade> nextGrade() {
        return this.grade.nextGrade();
    }

    public void increaseOrderAmount(int amount) {
        this.orderCount++;
        this.amount = amount;
    }
}
