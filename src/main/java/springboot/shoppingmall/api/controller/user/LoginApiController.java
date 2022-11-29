package springboot.shoppingmall.api.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.dto.user.LoginRequest;
import springboot.shoppingmall.dto.user.LoginResponse;
import springboot.shoppingmall.service.user.LoginService;
import springboot.shoppingmall.utils.login.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginApiController {

    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){
        //LoginResponse login = loginService.login(loginRequest);
        //log.info("loginApiController : login user={}", login);

        User user = loginService.login2(loginRequest);

        // jwtTokenProvider 토큰 생성
        return ResponseEntity.ok(jwtTokenProvider.createToken(user));
    }
}
