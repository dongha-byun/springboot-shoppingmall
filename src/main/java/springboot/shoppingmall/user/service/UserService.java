package springboot.shoppingmall.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;
import springboot.shoppingmall.user.dto.FindIdResponse;
import springboot.shoppingmall.user.dto.FindPwResponse;
import springboot.shoppingmall.user.controller.request.SignUpRequest;
import springboot.shoppingmall.user.dto.UserEditRequest;
import springboot.shoppingmall.user.dto.UserGradeInfoDto;
import springboot.shoppingmall.user.dto.UserResponse;
import springboot.shoppingmall.user.domain.UserRepository;
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
        User user = userRepository.save(createDto.toEntity());
        return UserResponse.of(user);
    }

    public UserResponse findUser(Long id){
        User user = userFinder.findUserById(id);
        return UserResponse.of(user);
    }
    public FindIdResponse findId(String name, String telNo) {
        User user = userRepository.findEmailByNameAndTelNo(name, telNo);
        String maskingLoginId = MaskingUtil.maskingLoginId(user.getEmail());

        return new FindIdResponse(maskingLoginId);
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
