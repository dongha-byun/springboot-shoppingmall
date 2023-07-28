package springboot.shoppingmall.coupon.domain;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.OrderSequence;
import springboot.shoppingmall.order.domain.OrderSequenceRepository;
import springboot.shoppingmall.utils.DateUtils;

@RequiredArgsConstructor
@Component
public class OrderCodeCreator {
    private final OrderSequenceRepository orderSequenceRepository;

    @Transactional
    public String createOrderCode() {
        LocalDateTime now = LocalDateTime.now();
        String yyyyMMdd = DateUtils.toStringOfLocalDateTIme(now, "yyyyMMdd");
        OrderSequence orderSequence = orderSequenceRepository.findByDate(yyyyMMdd)
                .orElseGet(() -> OrderSequence.createSequence(now));
        if (orderSequence.isNew()) {
            orderSequenceRepository.save(orderSequence);
        }
        return orderSequence.generateOrderCode();
    }
}
