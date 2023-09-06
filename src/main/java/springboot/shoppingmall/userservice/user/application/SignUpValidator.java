package springboot.shoppingmall.userservice.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.userservice.user.application.dto.SignUpRequestDto;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserRepository;

@RequiredArgsConstructor
@Component
public class SignUpValidator {
    private final UserRepository userRepository;

    public void validateSignUp(SignUpRequestDto signUpRequestDto) {
        checkPasswordIsSame(signUpRequestDto.getPassword(), signUpRequestDto.getConfirmPassword());
        checkExistsSameEmail(signUpRequestDto.getEmail());
    }

    private void checkPasswordIsSame(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않아 회원가입에 실패했습니다.");
        }
    }

    private void checkExistsSameEmail(String email) {
        User originUser = userRepository.findUserByLoginInfoEmail(email).orElse(null);
        if(originUser != null) {
            throw new IllegalArgumentException("이미 가입된 정보가 있습니다.");
        }
    }
}
