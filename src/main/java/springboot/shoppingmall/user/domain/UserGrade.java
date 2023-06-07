package springboot.shoppingmall.user.domain;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum UserGrade {
    NORMAL("일반회원", 0, 0),
    REGULAR("단골회원", 10, 50000),
    VIP("VIP", 50, 150000),
    VVIP("VVIP", 100, 300000);

    private final String gradeName;
    private final int minOrderCondition;
    private final int minAmountCondition;

    UserGrade(String gradeName, int minOrderCondition, int minAmountCondition) {
        this.gradeName = gradeName;
        this.minOrderCondition = minOrderCondition;
        this.minAmountCondition = minAmountCondition;
    }

    public Optional<UserGrade> nextGrade() {
        int currentOrdinal = this.ordinal();
        return Arrays.stream(UserGrade.values())
                .filter(
                        userGrade -> userGrade.ordinal() == (currentOrdinal + 1)
                )
                .findAny();
    }
}
