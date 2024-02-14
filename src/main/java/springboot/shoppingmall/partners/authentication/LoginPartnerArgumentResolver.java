package springboot.shoppingmall.partners.authentication;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import springboot.shoppingmall.authorization.GatewayConstants;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginPartnerArgumentResolver implements HandlerMethodArgumentResolver {

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

        String xGatewaySubject = request.getHeader(GatewayConstants.GATEWAY_HEADER);
        if(!StringUtils.hasText(xGatewaySubject)) {
            throw new IllegalArgumentException("Gateway Subject is not exists!");
        }

        return new AuthorizedPartner(Long.parseLong(xGatewaySubject));
    }
}
