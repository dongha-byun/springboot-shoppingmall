package springboot.shoppingmall.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;
import springboot.shoppingmall.user.dto.FindPwResponse;
import springboot.shoppingmall.user.dto.UserEditRequest;
import springboot.shoppingmall.user.dto.UserGradeInfoDto;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.service.dto.FindEmailRequestDto;
import springboot.shoppingmall.user.service.dto.FindEmailResultDto;
import springboot.shoppingmall.user.service.dto.SignUpRequestDto;
import springboot.shoppingmall.user.service.dto.UserDto;
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
