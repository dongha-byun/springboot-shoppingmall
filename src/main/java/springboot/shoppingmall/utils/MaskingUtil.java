package springboot.shoppingmall.utils;

public class MaskingUtil {

    public static final String MASKING_WILD_CARD = "*";

    public static String maskingName(String input) {
        String substring = input.substring(1, input.length()-1);
        return input.replace(substring, MASKING_WILD_CARD.repeat(substring.length()));
    }

    public static String maskingLoginId(String loginId) {
        int idLength = loginId.length();
        String maskingString = MASKING_WILD_CARD.repeat(idLength-2);

        String maskingTarget = loginId.substring(2, idLength);

        return loginId.replace(maskingTarget, maskingString);
    }


    public static String maskingEmail(String email) {
        String[] separatedEmail = email.split("@");
        String id = separatedEmail[0];
        String maskingString = MASKING_WILD_CARD.repeat(id.length() - 2);
        String maskingTarget = id.substring(2, id.length());

        String maskedId = id.replace(maskingTarget, maskingString);
        return maskedId.concat("@".concat(separatedEmail[1]));
    }
}
