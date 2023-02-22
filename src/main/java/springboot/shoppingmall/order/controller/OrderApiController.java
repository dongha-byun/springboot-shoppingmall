package springboot.shoppingmall.order.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.order.dto.OrderExchangeRequest;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.dto.OrderRefundRequest;
import springboot.shoppingmall.order.dto.OrderStatusChangeRequest;
import springboot.shoppingmall.order.service.OrderService;

/**
 * 서비스 내에서 호출되는 API 명세
 */
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

    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@AuthenticationStrategy AuthorizedUser user,
                                                     @PathVariable("id") Long orderId) {
        OrderResponse order = orderService.cancel(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{id}/outing")
    public ResponseEntity<OrderResponse> outingOrder(@AuthenticationStrategy AuthorizedUser user,
                                                     @PathVariable("id") Long orderId) {
        OrderResponse order = orderService.outing(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{id}/finish")
    public ResponseEntity<OrderResponse> finishOrder(@AuthenticationStrategy AuthorizedUser user,
                                                     @PathVariable("id") Long orderId) {
        OrderResponse order = orderService.finish(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{id}/refund")
    public ResponseEntity<OrderResponse> refundOrder(@AuthenticationStrategy AuthorizedUser user,
                                                     @PathVariable("id") Long orderId,
                                                     @RequestBody OrderRefundRequest refundRequest) {
        OrderResponse orderResponse = orderService.refund(orderId, refundRequest.getRefundReason());
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/orders/{id}/exchange")
    public ResponseEntity<OrderResponse> exchangeOrder(@AuthenticationStrategy AuthorizedUser user,
                                                       @PathVariable("id") Long orderId,
                                                       @RequestBody OrderExchangeRequest exchangeRequest) {
        OrderResponse orderResponse = orderService.exchange(orderId, exchangeRequest.getExchangeReason());
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/orders/{id}/checking")
    public ResponseEntity<OrderResponse> checkingOrder(@AuthenticationStrategy AuthorizedUser user,
                                                       @PathVariable("id") Long orderId) {
        OrderResponse order = orderService.checking(orderId);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/orders/{id}/refund-end")
    public ResponseEntity<OrderResponse> refundEndOrder(@AuthenticationStrategy AuthorizedUser user,
                                                        @PathVariable("id") Long orderId) {
        OrderResponse order = orderService.refundEnd(orderId);
        return ResponseEntity.ok(order);
    }
}
