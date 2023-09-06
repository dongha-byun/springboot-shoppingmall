package springboot.shoppingmall.user.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.DeliveryRepository;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserFinder;
import springboot.shoppingmall.user.dto.DeliveryRequest;
import springboot.shoppingmall.user.dto.DeliveryResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserFinder userFinder;

    @Transactional
    public DeliveryResponse create(Long userId, DeliveryRequest deliveryRequest) {
        User user = userFinder.findUserById(userId);
        Delivery delivery = deliveryRepository.save(DeliveryRequest.to(deliveryRequest).createBy(user));
        return DeliveryResponse.of(delivery);
    }

    @Transactional
    public void delete(Long userId, Long deliveryId) {
        User user = userFinder.findUserById(userId);
        Delivery delivery = findDeliveryById(deliveryId);

        user.removeDelivery(delivery);
    }

    private Delivery findDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(
                        () -> new IllegalArgumentException("배송지 조회 실패")
                );
    }

    public List<DeliveryResponse> findAllDelivery(Long userId) {
        User user = userFinder.findUserById(userId);
        return user.getDeliveries().stream()
                .map(DeliveryResponse::of).collect(Collectors.toList());
    }
}
