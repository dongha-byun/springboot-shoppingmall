package springboot.shoppingmall.api.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.dto.user.SignUpRequest;
import springboot.shoppingmall.service.user.UserService;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody SignUpRequest signUpRequest){
        Long userId = userService.signUp(signUpRequest);
        User user = userService.findUser(userId);
        return ResponseEntity.ok(user);
    }
}
