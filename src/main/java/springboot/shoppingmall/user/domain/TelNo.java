package springboot.shoppingmall.user.domain;

import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TelNo {

    private static final String TEL_NO_REGEX_010 = "^010-\\d{4}-\\d{4}$";
    private static final String TEL_NO_REGEX_ANOTHER = "^01(?:1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
    @Column(nullable = false)
    private final String telNo;

    public TelNo(String telNo) {
        if(!Pattern.matches(TEL_NO_REGEX_010, telNo) && !Pattern.matches(TEL_NO_REGEX_ANOTHER, telNo)) {
            throw new IllegalArgumentException("연락처 형태의 데이터가 아닙니다.");
        }
        this.telNo = telNo;
    }
}