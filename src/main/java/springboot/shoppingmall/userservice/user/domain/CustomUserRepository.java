package springboot.shoppingmall.userservice.user.domain;

import java.util.List;

public interface CustomUserRepository {
    User findEmailByNameAndTelNo(String name, String telNo);
    User findUserByNameAndTelNoAndEmail(String name, String telNo, String email);

    List<User> findUserOverTheUserGrade(List<UserGrade> targetGrades);
}
