package springboot.shoppingmall.user.domain;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository{

    private final EntityManager em;

    @Override
    public User findLoginIdByNameAndTelNo(String name, String telNo) {
        return em.createQuery("select u from User u "
                + "where 1=1 "
                + "and u.userName = :userName "
                + "and u.telNo = :telNo", User.class)
                .setParameter("userName", name)
                .setParameter("telNo", new TelNo(telNo))
                .getSingleResult();
    }

    @Override
    public User findUserByNameAndTelNoAndLoginId(String name, String telNo, String loginId) {
        return em.createQuery("select u from User u "
                        + "where 1=1 "
                        + "and u.userName = :userName "
                        + "and u.loginInfo.loginId = :loginId "
                        + "and u.telNo = :telNo", User.class)
                .setParameter("userName", name)
                .setParameter("telNo", new TelNo(telNo))
                .setParameter("loginId", loginId)
                .getSingleResult();
    }

    @Override
    public List<User> findUserOverTheUserGrade(List<UserGrade> targetGrades) {
        return em.createQuery("select u from User u "
                        + "where 1=1 "
                        + "and u.userGradeInfo.grade in :targetGrades ", User.class)
                .setParameter("targetGrades", targetGrades)
                .getResultList();
    }
}
