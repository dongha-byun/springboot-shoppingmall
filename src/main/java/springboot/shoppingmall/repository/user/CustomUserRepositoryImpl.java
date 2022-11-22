package springboot.shoppingmall.repository.user;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import springboot.shoppingmall.domain.user.User;
import springboot.shoppingmall.dto.user.FindIdRequest;
import springboot.shoppingmall.dto.user.FindPwRequest;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository{

    private final EntityManager em;

    @Override
    public User findLoginIdByNameAndTelNo(FindIdRequest findIdRequest) {
        return em.createQuery("select u from User u "
                + "where 1=1 "
                + "and u.userName = :userName "
                + "and u.telNo = :telNo", User.class)
                .setParameter("userName", findIdRequest.getName())
                .setParameter("telNo", findIdRequest.getTelNo())
                .getSingleResult();
    }

    @Override
    public User findUserByNameAndTelNoAndLoginId(FindPwRequest findPwRequest) {
        return em.createQuery("select u from User u "
                        + "where 1=1 "
                        + "and u.userName = :userName "
                        + "and u.loginId = :loginId "
                        + "and u.telNo = :telNo", User.class)
                .setParameter("userName", findPwRequest.getName())
                .setParameter("telNo", findPwRequest.getTelNo())
                .setParameter("loginId", findPwRequest.getLoginId())
                .getSingleResult();
    }
}
