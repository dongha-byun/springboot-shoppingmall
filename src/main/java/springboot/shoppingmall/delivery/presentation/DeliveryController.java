package springboot.shoppingmall.delivery.presentation;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.delivery.presentation.request.DeliveryRequest;
import springboot.shoppingmall.delivery.presentation.response.DeliveryResponse;
import springboot.shoppingmall.delivery.application.DeliveryService;

@RequiredArgsConstructor
@RestController
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/delivery")
    public ResponseEntity<List<DeliveryResponse>> findAllDelivery(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo){
        List<DeliveryResponse> deliveries = deliveryService.findAllDelivery(gatewayAuthInfo.getUserId());
        return ResponseEntity.ok(deliveries);
    }

    @DeleteMapping("/delivery/{id}")
    public ResponseEntity<DeliveryResponse> deleteDelivery(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                           @PathVariable("id") Long deliveryId){
        deliveryService.delete(gatewayAuthInfo.getUserId(), deliveryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delivery")
    public ResponseEntity<DeliveryResponse> createDelivery(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                           @RequestBody DeliveryRequest deliveryRequest){
        DeliveryResponse deliveryResponse = deliveryService.create(gatewayAuthInfo.getUserId(), deliveryRequest);
        return ResponseEntity.created(URI.create("/delivery/"+deliveryResponse.getId())).body(deliveryResponse);
    }
}
