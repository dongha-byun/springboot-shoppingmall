package springboot.shoppingmall.authorization.argument;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.authorization.service.AuthService;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationStrategyArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TYPE = "Bearer";
    private final AuthService authService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(AuthenticationStrategy.class);
        boolean isAuthorizedUserType = AuthorizedUser.class.equals(parameter.getParameterType());

        return hasParameterAnnotation && isAuthorizedUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null){
            throw new IllegalArgumentException();
        }

        String token = parsingTokenInRequest(request);
        return authService.getAuthorizedUser(token);
    }

    public String parsingTokenInRequest(HttpServletRequest request){
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
