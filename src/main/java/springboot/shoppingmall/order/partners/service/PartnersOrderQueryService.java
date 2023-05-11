package springboot.shoppingmall.order.partners.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryRepository;
import springboot.shoppingmall.order.partners.domain.PartnersOrderQueryType;
import springboot.shoppingmall.order.partners.dto.PartnersOrderQueryDto;
import springboot.shoppingmall.order.partners.dto.PartnersReadyOrderQueryDto;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PartnersOrderQueryService {

    private final PartnersOrderQueryRepository partnersOrderQueryRepository;

    public List<PartnersReadyOrderQueryDto> findPartnersReadyOrders(Long partnersId, PartnersOrderQueryType type,
                                                              LocalDateTime startDate, LocalDateTime endDate) {
        return partnersOrderQueryRepository.findPartnersReadyOrders(partnersId, type, startDate, endDate);
    }
}
