package springboot.shoppingmall.user.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.user.controller.request.FindEmailRequest;
import springboot.shoppingmall.user.controller.request.FindEmailResultResponse;
import springboot.shoppingmall.user.dto.FindPwRequest;
import springboot.shoppingmall.user.dto.FindPwResponse;
import springboot.shoppingmall.user.controller.request.SignUpRequest;
import springboot.shoppingmall.user.dto.UserEditRequest;
import springboot.shoppingmall.user.dto.UserGradeInfoDto;
import springboot.shoppingmall.user.dto.UserGradeInfoResponse;
import springboot.shoppingmall.user.dto.UserResponse;
import springboot.shoppingmall.user.service.UserService;
import springboot.shoppingmall.user.service.dto.FindEmailRequestDto;
import springboot.shoppingmall.user.service.dto.FindEmailResultDto;
import springboot.shoppingmall.user.service.dto.SignUpRequestDto;
import springboot.shoppingmall.user.service.dto.UserDto;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(@RequestBody SignUpRequest signUpRequest){
        SignUpRequestDto signUpRequestDto = signUpRequest.toDto();
        UserDto userDto = userService.signUp(signUpRequestDto);
        UserResponse response = UserResponse.of(userDto);

        return ResponseEntity.created(URI.create("/user/"+response.getId())).body(response);
    }

    @GetMapping("/find-email")
    public ResponseEntity<FindEmailResultResponse> findEmail(@RequestBody FindEmailRequest findEmailRequest){
        FindEmailRequestDto findEmailRequestDto = findEmailRequest.toDto();
        FindEmailResultDto resultDto = userService.findEmail(findEmailRequestDto);

        return ResponseEntity.ok().body(FindEmailResultResponse.of(resultDto));
    }

    @GetMapping("/find-pw")
    public ResponseEntity<FindPwResponse> findPw(@RequestBody FindPwRequest findPwRequest){
        FindPwResponse response =
                userService.findPw(findPwRequest.getName(), findPwRequest.getTelNo(), findPwRequest.getEmail());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> findUser(@AuthenticationStrategy AuthorizedUser user){
        UserDto dto = userService.findUser(user.getId());
        UserResponse response = UserResponse.of(dto);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/user")
    public ResponseEntity<UserResponse> updateUser(@AuthenticationStrategy AuthorizedUser user,
                                                   @RequestBody UserEditRequest userRequest){
        UserDto userDto = userService.editUser(user.getId(), userRequest);
        UserResponse response = UserResponse.of(userDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/grade-info")
    public ResponseEntity<UserGradeInfoResponse> findUserGradeInfo(@AuthenticationStrategy AuthorizedUser user) {
        UserGradeInfoDto userGradeInfo = userService.getUserGradeInfo(user.getId());
        return ResponseEntity.ok().body(UserGradeInfoResponse.to(userGradeInfo));
    }
}
