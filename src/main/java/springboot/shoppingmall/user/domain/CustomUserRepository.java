package springboot.shoppingmall.user.domain;

import springboot.shoppingmall.user.dto.FindIdRequest;
import springboot.shoppingmall.user.dto.FindPwRequest;

public interface CustomUserRepository {
    User findLoginIdByNameAndTelNo(FindIdRequest findIdRequest);
    User findUserByNameAndTelNoAndLoginId(FindPwRequest findPwRequest);
}
