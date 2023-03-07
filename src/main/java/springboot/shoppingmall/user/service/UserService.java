package springboot.shoppingmall.user.service;

import static springboot.shoppingmall.user.dto.UserEditRequest.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;
import springboot.shoppingmall.user.dto.FindIdRequest;
import springboot.shoppingmall.user.dto.FindIdResponse;
import springboot.shoppingmall.user.dto.FindPwRequest;
import springboot.shoppingmall.user.dto.FindPwResponse;
import springboot.shoppingmall.user.dto.SignUpRequest;
import springboot.shoppingmall.user.dto.UserEditRequest;
import springboot.shoppingmall.user.dto.UserResponse;
import springboot.shoppingmall.user.domain.UserRepository;
import springboot.shoppingmall.utils.MaskingUtil;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserFinder userFinder;
    @Transactional
    public UserResponse signUp(SignUpRequest signUpRequest){
        User user = userRepository.save(SignUpRequest.to(signUpRequest));
        return UserResponse.of(user);
    }

    public UserResponse findUser(Long id){
        User user = userFinder.findUserById(id);
        return UserResponse.of(user);
    }
    public FindIdResponse findId(String name, String telNo) {
        User user = userRepository.findLoginIdByNameAndTelNo(name, telNo);
        String maskingLoginId = MaskingUtil.maskingLoginId(user.getLoginId());

        return new FindIdResponse(maskingLoginId);
    }
    public FindPwResponse findPw(FindPwRequest findPwRequest) {
        User user = userRepository.findUserByNameAndTelNoAndLoginId(findPwRequest);
        return FindPwResponse.of(user);
    }

    @Transactional
    public UserResponse editUser(Long userId, UserEditRequest userEditRequest) {
        User user = userFinder.findUserById(userId);

        user.updateUser(to(userEditRequest));

        return UserResponse.of(user);
    }
}
