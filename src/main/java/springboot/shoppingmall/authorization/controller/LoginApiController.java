package springboot.shoppingmall.authorization.controller;

import static springboot.shoppingmall.authorization.service.AuthorizationExtractor.parsingToken;

import java.util.Enumeration;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.authorization.service.AuthService;
import springboot.shoppingmall.authorization.dto.LoginRequest;

@RequiredArgsConstructor
@Slf4j
@RestController
public class LoginApiController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest,
                                               HttpServletRequest request){
        TokenResponse tokenResponse = authService.login(loginRequest, request.getRemoteHost());

        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/login-refresh")
    public ResponseEntity<TokenResponse> loginRefresh(HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();
        Iterator<String> stringIterator = headerNames.asIterator();
        while(stringIterator.hasNext()){
            String header = stringIterator.next();
            log.info("{} : {}", header, request.getHeader(header));
        }
        log.info("login-refresh");
        String token = parsingToken(request);
        TokenResponse tokenResponse = authService.reCreateAccessToken(token, request.getRemoteHost());
        return ResponseEntity.ok(tokenResponse);
    }
}
