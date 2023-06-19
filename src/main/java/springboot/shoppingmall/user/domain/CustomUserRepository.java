package springboot.shoppingmall.user.domain;

import java.util.List;
import springboot.shoppingmall.user.dto.FindIdRequest;
import springboot.shoppingmall.user.dto.FindPwRequest;

public interface CustomUserRepository {
    User findLoginIdByNameAndTelNo(String name, String telNo);
    User findUserByNameAndTelNoAndLoginId(String name, String telNo, String loginId);

    List<User> findUserOverTheUserGrade(List<UserGrade> targetGrades);
}
