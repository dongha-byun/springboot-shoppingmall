package springboot.shoppingmall.order.partners.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartnersOrderQueryService {

    private final PartnersOrderQueryRepository partnersOrderQueryRepository;

    public List<PartnersOrderQueryDto> findPartnersOrder(Long partnersId, OrderStatus status,
                                                         LocalDateTime startDate, LocalDateTime endDate) {
        return partnersOrderQueryRepository.findPartnersOrders(partnersId, status, startDate, endDate);
    }
}
