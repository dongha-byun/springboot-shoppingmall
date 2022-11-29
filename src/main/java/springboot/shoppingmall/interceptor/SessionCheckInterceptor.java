package springboot.shoppingmall.interceptor;

import java.util.Enumeration;
import java.util.Iterator;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import springboot.shoppingmall.utils.login.JwtTokenProvider;

@Slf4j
public class SessionCheckInterceptor implements HandlerInterceptor {

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String jwtToken = jwtTokenProvider.resolveToken(request);
        if(StringUtils.hasText(jwtToken) && jwtTokenProvider.validateExpireToken(jwtToken) ){

            String user = jwtTokenProvider.getUser(jwtToken);
            log.info("user = {}", user);

            return true;
        }

//        response.setHeader("access-control-allow-origin", "http://localhost:3000");
//        response.setHeader("access-control-allow-headers", "*");

        return false;
    }
}
