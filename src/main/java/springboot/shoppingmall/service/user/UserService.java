package springboot.shoppingmall.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.dto.user.FindIdRequest;
import springboot.shoppingmall.dto.user.FindIdResponse;
import springboot.shoppingmall.dto.user.FindPwRequest;
import springboot.shoppingmall.dto.user.FindPwResponse;
import springboot.shoppingmall.dto.user.SignUpRequest;
import springboot.shoppingmall.dto.user.UserRequest;
import springboot.shoppingmall.dto.user.UserResponse;
import springboot.shoppingmall.repository.user.UserRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Transactional
    public Long signUp(SignUpRequest signUpRequest){
        User user = userRepository.save(SignUpRequest.to(signUpRequest));
        return user.getId();
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
