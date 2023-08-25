package springboot.shoppingmall.user.domain;

import java.util.List;
import springboot.shoppingmall.user.dto.FindIdRequest;
import springboot.shoppingmall.user.dto.FindPwRequest;

public interface CustomUserRepository {
    User findEmailByNameAndTelNo(String name, String telNo);
    User findUserByNameAndTelNoAndEmail(String name, String telNo, String email);

    List<User> findUserOverTheUserGrade(List<UserGrade> targetGrades);
}
