package springboot.shoppingmall.utils;

import java.time.LocalDateTime;

public abstract class DateUtils {
    private static final Integer YEAR_POSITION = 0;
    private static final Integer MONTH_POSITION = 1;
    private static final Integer DAY_POSITION = 2;
    private static final String DELIMITER = "-";

    public static LocalDateTime getLocalDateTime(String dateString, int hour, int minute, int second) {
        String[] dateArr = dateString.split(DELIMITER);
        if(dateArr.length != 3) {
            throw new IllegalArgumentException("날짜 데이터는 yyyy-MM-dd 형태로 와야 합니다.");
        }
        return LocalDateTime.of(
                Integer.parseInt(dateArr[YEAR_POSITION]),
                Integer.parseInt(dateArr[MONTH_POSITION]),
                Integer.parseInt(dateArr[DAY_POSITION]),
                hour, minute, second
        );
    }
}
