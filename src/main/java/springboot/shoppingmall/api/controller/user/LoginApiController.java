package springboot.shoppingmall.api.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.api.util.ApiResult;
import springboot.shoppingmall.dto.user.LoginRequest;
import springboot.shoppingmall.dto.user.LoginResponse;
import springboot.shoppingmall.service.user.LoginService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){
        LoginResponse login = loginService.login(loginRequest);
        log.info("loginApiController : login user={}", login);
        return ResponseEntity.ok(login);
    }
}
