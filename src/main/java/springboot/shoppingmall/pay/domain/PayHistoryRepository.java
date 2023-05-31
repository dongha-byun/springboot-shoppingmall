package springboot.shoppingmall.pay.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayHistoryRepository extends JpaRepository<PayHistory, Long> {
    Optional<PayHistory> findByOrderId(Long orderId);
}
