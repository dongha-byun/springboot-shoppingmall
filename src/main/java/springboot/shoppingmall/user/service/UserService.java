package springboot.shoppingmall.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.dto.FindIdRequest;
import springboot.shoppingmall.user.dto.FindIdResponse;
import springboot.shoppingmall.user.dto.FindPwRequest;
import springboot.shoppingmall.user.dto.FindPwResponse;
import springboot.shoppingmall.user.dto.SignUpRequest;
import springboot.shoppingmall.user.dto.UserRequest;
import springboot.shoppingmall.user.dto.UserResponse;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Transactional
    public UserResponse signUp(SignUpRequest signUpRequest){
        User user = userRepository.save(SignUpRequest.to(signUpRequest));
        return UserResponse.of(user);
    }

    public UserResponse findUser(Long id){
        return UserResponse.of(userRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자 정보 조회 실패")
                ));
    }
    public FindIdResponse findId(FindIdRequest findIdRequest) {
        User user = userRepository.findLoginIdByNameAndTelNo(findIdRequest);
        return FindIdResponse.of(user);
    }
    public FindPwResponse findPw(FindPwRequest findPwRequest) {
        User user = userRepository.findUserByNameAndTelNoAndLoginId(findPwRequest);
        return FindPwResponse.of(user);
    }
    @Transactional
    public void editUser(Long id, UserRequest userRequest){
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자 조회 실패")
                );

        user.updateUser(userRequest);
    }
}
