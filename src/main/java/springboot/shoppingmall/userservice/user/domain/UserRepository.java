package springboot.shoppingmall.userservice.user.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findUserByLoginInfoEmail(String email);

}
