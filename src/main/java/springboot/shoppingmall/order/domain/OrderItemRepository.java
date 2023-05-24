package springboot.shoppingmall.order.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findOrderItemByInvoiceNumber(String invoiceNumber);
}
