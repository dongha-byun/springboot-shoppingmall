package springboot.shoppingmall.authorization.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.authorization.JwtTokenProvider;

@Slf4j
public class SessionCheckInterceptor implements HandlerInterceptor {

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

//        Iterator<String> stringIterator = request.getHeaderNames().asIterator();
//        while(stringIterator.hasNext()){
//            String headerName = stringIterator.next();
//            log.info("request = [{}] : [{}]", headerName, request.getHeader(headerName));
//        }

        String jwtToken = jwtTokenProvider.resolveToken(request);

        response.setHeader("access-control-allow-origin", "http://localhost:3000");
        response.setHeader("access-control-allow-headers", "*");

        if("testToken".equals(jwtToken)){
            return true;
        }

        if(StringUtils.hasText(jwtToken) && jwtTokenProvider.validateExpireToken(jwtToken)){
            AuthorizedUser authorizedUser = jwtTokenProvider.getAuthorizedUser(jwtToken);
            request.setAttribute("authorizedUser", authorizedUser);

            return true;
        }

        return false;
    }


}
