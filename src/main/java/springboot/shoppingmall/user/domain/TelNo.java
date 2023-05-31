package springboot.shoppingmall.user.domain;

import static springboot.shoppingmall.user.domain.TelNoRegexConstants.TEL_NO_REGEX_010;
import static springboot.shoppingmall.user.domain.TelNoRegexConstants.TEL_NO_REGEX_ANOTHER;

import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class TelNo {
    @Column(nullable = false)
    private String telNo;

    public TelNo(String telNo) {
        if(!Pattern.matches(TEL_NO_REGEX_010, telNo) && !Pattern.matches(TEL_NO_REGEX_ANOTHER, telNo)) {
            throw new IllegalArgumentException("연락처 형태의 데이터가 아닙니다.");
        }
        this.telNo = telNo;
    }
}
