package springboot.shoppingmall.order.controller;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.order.dto.CancelRequest;
import springboot.shoppingmall.order.dto.OrderExchangeRequest;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.order.dto.OrderRefundRequest;
import springboot.shoppingmall.order.service.OrderStatusChangeService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@RequiredArgsConstructor
@RestController
public class OrderStatusChangeController {
    private final OrderStatusChangeService orderStatusChangeService;

    @PutMapping("/orders/{orderId}/{orderItemId}/cancel")
    public ResponseEntity<OrderItemResponse> cancelOrder(@AuthenticationStrategy AuthorizedUser user,
                                                         @PathVariable("orderId") Long orderId,
                                                         @PathVariable("orderItemId") Long orderItemId,
                                                         @RequestBody CancelRequest cancelRequest) {
        OrderItemResponse response =
                orderStatusChangeService.cancel(orderId, orderItemId, LocalDateTime.now(), cancelRequest.getCancelReason());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/outing")
    public ResponseEntity<OrderItemResponse> outingOrder(@LoginPartner AuthorizedPartner partner,
                                                         @PathVariable("orderId") Long orderId,
                                                         @PathVariable("orderItemId") Long orderItemId) {
        OrderItemResponse response = orderStatusChangeService.outing(orderId, orderItemId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/finish")
    public ResponseEntity<OrderItemResponse> finishOrder(@AuthenticationStrategy AuthorizedUser user,
                                                         @PathVariable("orderId") Long orderId,
                                                         @PathVariable("orderItemId") Long orderItemId) {
        OrderItemResponse response = orderStatusChangeService.finish(orderId, orderItemId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/refund")
    public ResponseEntity<OrderItemResponse> refundOrder(@AuthenticationStrategy AuthorizedUser user,
                                                         @PathVariable("orderId") Long orderId,
                                                         @PathVariable("orderItemId") Long orderItemId,
                                                         @RequestBody OrderRefundRequest refundRequest) {

        OrderItemResponse response =
                orderStatusChangeService.refund(orderId, orderItemId, LocalDateTime.now(), refundRequest.getRefundReason());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/exchange")
    public ResponseEntity<OrderItemResponse> exchangeOrder(@AuthenticationStrategy AuthorizedUser user,
                                                           @PathVariable("orderId") Long orderId,
                                                           @PathVariable("orderItemId") Long orderItemId,
                                                           @RequestBody OrderExchangeRequest exchangeRequest) {

        OrderItemResponse response =
                orderStatusChangeService.exchange(orderId, orderItemId, LocalDateTime.now(), exchangeRequest.getExchangeReason());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/checking")
    public ResponseEntity<OrderItemResponse> checkingOrder(@AuthenticationStrategy AuthorizedUser user,
                                                           @PathVariable("orderId") Long orderId,
                                                           @PathVariable("orderItemId") Long orderItemId) {
        OrderItemResponse response = orderStatusChangeService.checking(orderId, orderItemId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/refund-end")
    public ResponseEntity<OrderItemResponse> refundEndOrder(@AuthenticationStrategy AuthorizedUser user,
                                                            @PathVariable("orderId") Long orderId,
                                                            @PathVariable("orderItemId") Long orderItemId) {
        OrderItemResponse response = orderStatusChangeService.refundEnd(orderId, orderItemId);
        return ResponseEntity.ok().body(response);
    }
}
