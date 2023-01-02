package springboot.shoppingmall.user.controller;

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
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.user.dto.DeliveryRequest;
import springboot.shoppingmall.user.dto.DeliveryResponse;
import springboot.shoppingmall.user.service.DeliveryService;

@RequiredArgsConstructor
@RestController
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/delivery")
    public ResponseEntity<List<DeliveryResponse>> findAllDelivery(@AuthenticationStrategy AuthorizedUser user){
        List<DeliveryResponse> deliveries = deliveryService.findAllDelivery(user.getId());
        return ResponseEntity.ok(deliveries);
    }

    @DeleteMapping("/delivery/{id}")
    public ResponseEntity<DeliveryResponse> deleteDelivery(@AuthenticationStrategy AuthorizedUser user,
                                                           @PathVariable("id") Long deliveryId){
        deliveryService.delete(user.getId(), deliveryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delivery")
    public ResponseEntity<DeliveryResponse> createDelivery(@AuthenticationStrategy AuthorizedUser user,
                                                           @RequestBody DeliveryRequest deliveryRequest){
        DeliveryResponse deliveryResponse = deliveryService.create(user.getId(), deliveryRequest);
        return ResponseEntity.created(URI.create("/delivery/"+deliveryResponse.getId())).body(deliveryResponse);
    }
}
