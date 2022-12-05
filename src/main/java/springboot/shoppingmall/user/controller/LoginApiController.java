package springboot.shoppingmall.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.authorization.AuthorizationResponse;
import springboot.shoppingmall.user.dto.LoginRequest;
import springboot.shoppingmall.user.service.LoginService;
import springboot.shoppingmall.authorization.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginApiController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){
        User user = loginService.login(loginRequest);
        String token = jwtTokenProvider.createToken(user);
        long expiration = jwtTokenProvider.getExpiration(token);

        // jwtTokenProvider 토큰 생성
        return ResponseEntity.ok(new AuthorizationResponse(token, expiration));
    }
}
