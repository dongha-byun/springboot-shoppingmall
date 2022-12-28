package springboot.shoppingmall.authorization.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import springboot.shoppingmall.user.domain.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User saveUser);
}
