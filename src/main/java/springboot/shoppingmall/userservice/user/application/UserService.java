package springboot.shoppingmall.userservice.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.userservice.user.application.dto.FindEmailRequestDto;
import springboot.shoppingmall.userservice.user.application.dto.FindEmailResultDto;
import springboot.shoppingmall.userservice.user.application.dto.SignUpRequestDto;
import springboot.shoppingmall.userservice.user.application.dto.UserDto;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserFinder;
import springboot.shoppingmall.userservice.user.domain.UserRepository;
import springboot.shoppingmall.userservice.user.presentation.response.FindPwResponse;
import springboot.shoppingmall.userservice.user.presentation.request.UserEditRequest;
import springboot.shoppingmall.userservice.user.application.dto.UserGradeInfoDto;
import springboot.shoppingmall.utils.MaskingUtil;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFinder userFinder;
    private final SignUpValidator signUpValidator;

    @Transactional
    public UserDto signUp(SignUpRequestDto signUpRequestDto){
        signUpValidator.validateSignUp(signUpRequestDto);

        User user = userRepository.save(signUpRequestDto.toEntity());
        return UserDto.of(user);
    }

    public UserDto findUser(Long id){
        User user = userFinder.findUserById(id);
        return UserDto.of(user);
    }

    public FindEmailResultDto findEmail(FindEmailRequestDto findEmailRequestDto) {
        User user = userRepository.findEmailByNameAndTelNo(
                findEmailRequestDto.getName(),
                findEmailRequestDto.getTelNo()
        );
        String maskingEmail = MaskingUtil.maskingEmail(user.getEmail());

        return FindEmailResultDto.of(maskingEmail);
    }

    public FindPwResponse findPw(String name, String telNo, String email) {
        User user = userRepository.findUserByNameAndTelNoAndEmail(name, telNo, email);
        return FindPwResponse.of(user);
    }

    @Transactional
    public UserDto editUser(Long userId, UserEditRequest userEditRequest) {
        User user = userFinder.findUserById(userId);
        user.updateUser(userEditRequest.getTelNo(), userEditRequest.getPassword());

        return UserDto.of(user);
    }

    public UserGradeInfoDto getUserGradeInfo(Long userId) {
        User user = userFinder.findUserById(userId);
        return UserGradeInfoDto.of(user);
    }
}
