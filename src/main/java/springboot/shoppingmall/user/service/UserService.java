package springboot.shoppingmall.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;
import springboot.shoppingmall.user.dto.FindPwResponse;
import springboot.shoppingmall.user.dto.UserEditRequest;
import springboot.shoppingmall.user.dto.UserGradeInfoDto;
import springboot.shoppingmall.user.dto.UserResponse;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.user.service.dto.FindEmailRequestDto;
import springboot.shoppingmall.user.service.dto.FindEmailResultDto;
import springboot.shoppingmall.user.service.dto.UserCreateDto;
import springboot.shoppingmall.utils.MaskingUtil;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFinder userFinder;

    @Transactional
    public UserResponse signUp(UserCreateDto createDto){
        if(!createDto.getPassword().equals(createDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않아 회원가입에 실패했습니다.");
        }
        User originUser = userRepository.findUserByLoginInfoEmail(createDto.getEmail()).orElse(null);
        if(originUser != null) {
            throw new IllegalArgumentException("이미 가입된 정보가 있습니다.");
        }
        User user = userRepository.save(createDto.toEntity());
        return UserResponse.of(user);
    }

    public UserResponse findUser(Long id){
        User user = userFinder.findUserById(id);
        return UserResponse.of(user);
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
    public UserResponse editUser(Long userId, UserEditRequest userEditRequest) {
        User user = userFinder.findUserById(userId);
        user.updateUser(userEditRequest.getTelNo(), userEditRequest.getPassword());

        return UserResponse.of(user);
    }

    public UserGradeInfoDto getUserGradeInfo(Long userId) {
        User user = userFinder.findUserById(userId);
        return UserGradeInfoDto.of(user);
    }
}
