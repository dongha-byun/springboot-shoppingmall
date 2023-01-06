package springboot.shoppingmall.authorization.service;

import static springboot.shoppingmall.authorization.AuthorizationConstants.AUTHORIZATION;
import static springboot.shoppingmall.authorization.AuthorizationConstants.BEARER_TYPE;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

public class AuthorizationExtractor {

    public static String parsingToken(HttpServletRequest request){
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()){
            String value = headers.nextElement();
            if(value.startsWith(BEARER_TYPE)){
                return value.replace(BEARER_TYPE, "").trim();
            }
        }
        return null;
    }
}
