package springboot.shoppingmall.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.service.dto.UserCreateDto;

@RequiredArgsConstructor
@Component
public class SignUpValidator {
    private final UserRepository userRepository;

    public void validateSignUp(UserCreateDto createDto) {
        checkPasswordIsSame(createDto);
        checkExistsSameEmail(createDto);
    }

    private void checkPasswordIsSame(UserCreateDto createDto) {
        if(!createDto.getPassword().equals(createDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않아 회원가입에 실패했습니다.");
        }
    }

    private void checkExistsSameEmail(UserCreateDto createDto) {
        User originUser = userRepository.findUserByLoginInfoEmail(createDto.getEmail()).orElse(null);
        if(originUser != null) {
            throw new IllegalArgumentException("이미 가입된 정보가 있습니다.");
        }
    }
}
