package springboot.shoppingmall.providers.authentication;

import static springboot.shoppingmall.authorization.service.AuthorizationExtractor.*;

import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import springboot.shoppingmall.authorization.service.JwtTokenProvider;

@Slf4j
@RequiredArgsConstructor
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
