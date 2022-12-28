package springboot.shoppingmall.utils;

public class MaskingUtil {
    public static String maskString(String input) {
        String substring = input.substring(1, input.length()-1);
        String maskCard = "";
        for(int i=0; i<substring.length(); i++){
            maskCard += "*";
        }
        return input.replace(substring, maskCard);
    }
}
