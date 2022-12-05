package springboot.shoppingmall.user.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findUserByLoginId(String loginId);

}
