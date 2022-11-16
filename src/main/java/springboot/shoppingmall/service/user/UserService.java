package springboot.shoppingmall.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.dto.user.SignUpRequest;
import springboot.shoppingmall.repository.user.UserRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long signUp(SignUpRequest signUpRequest){
        User user = userRepository.save(SignUpRequest.to(signUpRequest));
        return user.getId();
    }

    @Transactional(readOnly = true)
    public User findUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("사용자 정보 조회 실패")
                );
    }
}
