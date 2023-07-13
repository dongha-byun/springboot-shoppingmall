package springboot.shoppingmall.coupon.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.coupon.domain.CouponQueryRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CouponQueryService {
    private final CouponQueryRepository queryRepository;

    public List<CouponQueryDto> findCouponAll(Long partnersId) {
        return queryRepository.findCouponAll(partnersId);
    }
}
