package springboot.shoppingmall.user.domain;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.user.dto.FindIdRequest;
import springboot.shoppingmall.user.dto.FindPwRequest;

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
                .setParameter("telNo", telNo)
                .getSingleResult();
    }

    @Override
    public User findUserByNameAndTelNoAndLoginId(String name, String telNo, String loginId) {
        return em.createQuery("select u from User u "
                        + "where 1=1 "
                        + "and u.userName = :userName "
                        + "and u.loginId = :loginId "
                        + "and u.telNo = :telNo", User.class)
                .setParameter("userName", name)
                .setParameter("telNo", telNo)
                .setParameter("loginId", loginId)
                .getSingleResult();
    }
}
