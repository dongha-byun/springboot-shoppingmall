package springboot.shoppingmall.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemResolutionHistoryRepository extends JpaRepository<OrderItemResolutionHistory, Long> {
}
