package springboot.shoppingmall.order.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSequenceRepository extends JpaRepository<OrderSequence, Long> {

    Optional<OrderSequence> findByDate(String date);
}
