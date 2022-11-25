package springboot.shoppingmall.api.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.dto.user.FindIdRequest;
import springboot.shoppingmall.dto.user.FindIdResponse;
import springboot.shoppingmall.dto.user.FindPwRequest;
import springboot.shoppingmall.dto.user.FindPwResponse;
import springboot.shoppingmall.dto.user.SignUpRequest;
import springboot.shoppingmall.dto.user.UserRequest;
import springboot.shoppingmall.dto.user.UserResponse;
import springboot.shoppingmall.service.user.UserService;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;
    @PostMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody SignUpRequest signUpRequest){
        userService.signUp(signUpRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-id")
    public ResponseEntity findId(@RequestBody FindIdRequest findIdRequest){
        FindIdResponse response = userService.findId(findIdRequest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find-pw")
    public ResponseEntity findPw(@RequestBody FindPwRequest findPwRequest){
        FindPwResponse response = userService.findPw(findPwRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity findUser(@PathVariable("id") Long id){
        UserResponse userResponse = userService.findUser(id);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity updateUser(@PathVariable("id") Long id,
                                     @RequestBody UserRequest userRequest){
        userService.editUser(id, userRequest);
        return ResponseEntity.ok().build();
    }
}
