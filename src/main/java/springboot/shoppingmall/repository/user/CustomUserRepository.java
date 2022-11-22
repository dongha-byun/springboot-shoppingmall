package springboot.shoppingmall.repository.user;

import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.dto.user.FindIdRequest;
import springboot.shoppingmall.dto.user.FindPwRequest;

public interface CustomUserRepository {
    User findLoginIdByNameAndTelNo(FindIdRequest findIdRequest);
    User findUserByNameAndTelNoAndLoginId(FindPwRequest findPwRequest);
}
