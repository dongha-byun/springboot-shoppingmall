package springboot.shoppingmall.userservice.user.infra;

import static springboot.shoppingmall.userservice.user.domain.QUser.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import springboot.shoppingmall.userservice.user.domain.UserQueryRepository;
import springboot.shoppingmall.userservice.user.domain.TelNo;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserGrade;

@Repository
public class JPAUserQueryRepository implements UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public JPAUserQueryRepository(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public User findEmailOf(String name, String telNo) {
        return jpaQueryFactory.selectFrom(user)
                .where(
                        user.userName.eq(name)
                                .and(
                                        user.telNo.eq(new TelNo(telNo))
                                )
                ).fetchOne();
    }

    @Override
    public User findUserOf(String name, String telNo, String email) {
        return jpaQueryFactory.selectFrom(user)
                .where(
                        user.userName.eq(name)
                                .and(user.telNo.eq(new TelNo(telNo)))
                                .and(user.loginInfo.email.eq(email))
                ).fetchOne();
    }

    @Override
    public List<User> findUserOverTheUserGrade(List<UserGrade> targetGrades) {
        return jpaQueryFactory.selectFrom(user)
                .where(
                        user.userGradeInfo.grade.in(targetGrades)
                ).fetch();
    }
}
