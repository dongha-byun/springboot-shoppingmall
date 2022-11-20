package springboot.shoppingmall.repository.user;

import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.dto.user.FindIdRequest;

public interface CustomUserRepository {

    User findLoginIdByNameAndTelNo(FindIdRequest findIdRequest);
}
