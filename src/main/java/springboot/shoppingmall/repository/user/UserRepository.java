package springboot.shoppingmall.repository.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import springboot.shoppingmall.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findUserByLoginId(String loginId);

}
