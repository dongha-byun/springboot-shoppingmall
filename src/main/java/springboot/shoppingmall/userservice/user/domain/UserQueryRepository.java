package springboot.shoppingmall.userservice.user.domain;

import java.util.List;

public interface UserQueryRepository {
    User findEmailOf(String name, String telNo);
    User findUserOf(String name, String telNo, String email);

    List<User> findUserOverTheUserGrade(List<UserGrade> targetGrades);
}
