package springboot.shoppingmall.userservice.user.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public enum UserGrade {
    NORMAL("일반회원", 0, 0, 1),
    REGULAR("단골회원", 10, 50000, 3),
    VIP("VIP", 50, 150000, 5),
    VVIP("VVIP", 100, 300000, 10);

    private final String gradeName;
    private final int minOrderCondition;
    private final int minAmountCondition;
    private final int discountRate;

    UserGrade(String gradeName, int minOrderCondition, int minAmountCondition, int discountRate) {
        this.gradeName = gradeName;
        this.minOrderCondition = minOrderCondition;
        this.minAmountCondition = minAmountCondition;
        this.discountRate = discountRate;
    }

    public Optional<UserGrade> nextGrade() {
        int currentOrdinal = this.ordinal();
        return Arrays.stream(UserGrade.values())
                .filter(
                        userGrade -> userGrade.ordinal() == (currentOrdinal + 1)
                )
                .findAny();
    }

    public List<UserGrade> overGrades() {
        int currentOrdinal = this.ordinal();
        return Arrays.stream(UserGrade.values())
                .filter(
                        userGrade -> userGrade.ordinal() >= currentOrdinal
                )
                .collect(Collectors.toList());
    }
}
