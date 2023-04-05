package springboot.shoppingmall.authorization.argument;

import static springboot.shoppingmall.authorization.service.AuthorizationExtractor.parsingToken;

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
    private final AuthService authService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(AuthenticationStrategy.class);
        boolean isAuthorizedUserType = AuthorizedUser.class.equals(parameter.getParameterType());

        return hasParameterAnnotation && isAuthorizedUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null){
            throw new IllegalArgumentException();
        }

        if(parameter.getParameterAnnotation(AuthenticationStrategy.class) == null) {
            throw new IllegalArgumentException();
        }

        boolean required = parameter.getParameterAnnotation(AuthenticationStrategy.class).required();
        if(!required){
            return new AuthorizedUser();
        }

        String token = parsingToken(request);
        return authService.getAuthorizedUser(token, request.getRemoteHost());
    }

}
