package springboot.shoppingmall.order.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.dto.OrderStatusChangeRequest;
import springboot.shoppingmall.order.service.OrderService;

@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@AuthenticationStrategy AuthorizedUser user,
                                                     @RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.createOrder(user.getId(), orderRequest);
        return ResponseEntity.created(URI.create("/orders/")).body(orderResponse);
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<OrderResponse> changeOrderStatus(@AuthenticationStrategy AuthorizedUser user,
                                                           @PathVariable("id") Long orderId,
                                                           @RequestBody OrderStatusChangeRequest changeRequest){
        OrderResponse orderResponse = orderService.changeOrderStatus(orderId, changeRequest.getStatus());
        return ResponseEntity.ok().body(orderResponse);
    }
}
