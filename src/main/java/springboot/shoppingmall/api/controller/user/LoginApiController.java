package springboot.shoppingmall.api.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.api.util.ApiResult;
import springboot.shoppingmall.dto.user.LoginRequest;
import springboot.shoppingmall.service.user.LoginService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginApiController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ApiResult login(@RequestBody LoginRequest loginRequest){
        User user = loginService.login(loginRequest.getLoginId(), loginRequest.getPassword());
        log.info("loginApiController : login user={}",user);
        return ApiResult.build()
                .returnCode("0")
                .message("로그인 성공");
    }
}
