package springboot.shoppingmall.coupon.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class UsingDuration {

    @Column(name = "from_date")
    private LocalDateTime fromDate;

    @Column(name = "to_date")
    private LocalDateTime toDate;

    public UsingDuration(LocalDateTime fromDate, LocalDateTime toDate) {
        validateDuration(fromDate, toDate);
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    private void validateDuration(LocalDateTime fromDate, LocalDateTime toDate) {
        if(fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("유효기간의 시작날짜가 종료날짜보다 이후로 설정될 수 없습니다.");
        }
    }
}
