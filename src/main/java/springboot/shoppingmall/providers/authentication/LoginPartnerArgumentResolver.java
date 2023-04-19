package springboot.shoppingmall.providers.authentication;

import static springboot.shoppingmall.authorization.service.AuthorizationExtractor.*;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import springboot.shoppingmall.authorization.service.JwtTokenProvider;

@RequiredArgsConstructor
@Component
public class LoginPartnerArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginPartner.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null) {
            throw new IllegalStateException("네트워크에 문제가 발생했습니다. 잠시 후, 다시 시도해주세요.");
        }

        String token = parsingToken(request);
        Long partnerId = jwtTokenProvider.getUserId(token);
        return new AuthorizedPartner(partnerId);
    }
}
