package springboot.shoppingmall.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.user.dto.FindIdRequest;
import springboot.shoppingmall.user.dto.FindIdResponse;
import springboot.shoppingmall.user.dto.FindPwRequest;
import springboot.shoppingmall.user.dto.FindPwResponse;
import springboot.shoppingmall.user.dto.SignUpRequest;
import springboot.shoppingmall.user.dto.UserRequest;
import springboot.shoppingmall.user.dto.UserResponse;
import springboot.shoppingmall.user.service.UserService;

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
