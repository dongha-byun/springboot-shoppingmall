package springboot.shoppingmall.authorization.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.authorization.service.AuthService;
import springboot.shoppingmall.authorization.dto.LoginRequest;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginApiController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest,
                                               HttpServletRequest request){
        TokenResponse tokenResponse = authService.login(loginRequest, request.getRemoteHost());

        return ResponseEntity.ok(tokenResponse);
    }
}
