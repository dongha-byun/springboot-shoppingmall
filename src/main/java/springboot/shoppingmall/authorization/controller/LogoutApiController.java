package springboot.shoppingmall.authorization.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.authorization.dto.TokenResponse;
import springboot.shoppingmall.authorization.service.AuthService;

@RequiredArgsConstructor
@RestController
public class LogoutApiController {

    private final AuthService authService;

    @PostMapping("/logout")
    public TokenResponse logout(@AuthenticationStrategy AuthorizedUser user){
        return authService.logout(user.getId());
    }
}
