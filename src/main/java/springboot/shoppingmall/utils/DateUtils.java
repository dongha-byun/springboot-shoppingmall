package springboot.shoppingmall.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class DateUtils {
    private static final Integer YEAR_POSITION = 0;
    private static final Integer MONTH_POSITION = 1;
    private static final Integer DAY_POSITION = 2;
    private static final String DELIMITER = "-";

    public static LocalDateTime getLocalDateTime(String dateString, int hour, int minute, int second) {
        if(hour < 0 || minute < 0 || second < 0) {
            throw new IllegalArgumentException("시/분/초 데이터는 음수일 수 없습니다.");
        }

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

    public static String toStringOfLocalDateTIme(LocalDateTime localDateTime) {
        return toStringOfLocalDateTIme(localDateTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static String toStringOfLocalDateTIme(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    public static LocalDateTime toStartDate(String dateStr) {
        return getLocalDateTime(dateStr, 0, 0, 0);
    }

    public static LocalDateTime toEndDate(String dateStr) {
        return getLocalDateTime(dateStr, 23, 59, 59);
    }
}
