package springboot.shoppingmall.api.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.api.util.ApiResult;
import springboot.shoppingmall.dto.user.FindIdRequest;
import springboot.shoppingmall.dto.user.FindIdResponse;
import springboot.shoppingmall.dto.user.SignUpRequest;
import springboot.shoppingmall.service.user.UserService;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ApiResult signUp(@RequestBody SignUpRequest signUpRequest){
        userService.signUp(signUpRequest);

        return ApiResult.build()
                .returnCode("0")
                .message("회원가입에 성공하였습니다.");
    }

    @GetMapping("/find-id")
    public ApiResult findId(@RequestBody FindIdRequest findIdRequest){
        FindIdResponse response = userService.findId(findIdRequest);

        return ApiResult.build()
                .returnCode("0")
                .message("아이디 조회에 성공했습니다.")
                .body(response);
    }
}
