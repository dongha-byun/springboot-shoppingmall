package springboot.shoppingmall.utils;

public class MaskingUtil {

    private static final String MASKING_WILD_CARD = "*";
    private static final String EMAIL_DELIMITER = "@";
    private static final int EMAIL_ID_INDEX = 0;
    private static final int EMAIL_ADDRESS_INDEX = 1;

    public static String maskingName(String input) {
        String substring = input.substring(1, input.length()-1);
        return input.replace(substring, MASKING_WILD_CARD.repeat(substring.length()));
    }

    public static String maskingEmail(String email) {
        String[] separatedEmail = email.split(EMAIL_DELIMITER);
        String id = separatedEmail[EMAIL_ID_INDEX];
        String maskingString = MASKING_WILD_CARD.repeat(id.length() - 2);
        String maskingTarget = id.substring(2);

        String maskedId = id.replace(maskingTarget, maskingString);
        return maskedId.concat(EMAIL_DELIMITER.concat(separatedEmail[EMAIL_ADDRESS_INDEX]));
    }
}
