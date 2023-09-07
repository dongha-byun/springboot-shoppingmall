package springboot.shoppingmall.delivery.application;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.delivery.domain.Delivery;
import springboot.shoppingmall.delivery.domain.DeliveryRepository;
import springboot.shoppingmall.delivery.presentation.request.DeliveryRequest;
import springboot.shoppingmall.delivery.presentation.response.DeliveryResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public DeliveryResponse create(Long userId, DeliveryRequest deliveryRequest) {
        Delivery delivery = deliveryRepository.save(DeliveryRequest.to(deliveryRequest).createBy(userId));
        return DeliveryResponse.of(delivery);
    }

    @Transactional
    public void delete(Long userId, Long deliveryId) {
        Delivery delivery = findDeliveryById(deliveryId);

        if(delivery.getUserId().equals(userId)) {
            deliveryRepository.delete(delivery);
        }
    }

    private Delivery findDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(
                        () -> new IllegalArgumentException("배송지 조회 실패")
                );
    }

    public List<DeliveryResponse> findAllDelivery(Long userId) {
        List<Delivery> deliveries = deliveryRepository.findAll().stream()
                .filter(
                        delivery -> delivery.getUserId().equals(userId)
                ).collect(Collectors.toList());
        return deliveries.stream()
                .map(DeliveryResponse::of).collect(Collectors.toList());
    }
}
