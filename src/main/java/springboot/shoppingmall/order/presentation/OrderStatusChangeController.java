package springboot.shoppingmall.order.presentation;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.order.dto.CancelRequest;
import springboot.shoppingmall.order.dto.OrderExchangeRequest;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.order.dto.OrderRefundRequest;
import springboot.shoppingmall.order.application.OrderStatusChangeService;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.partners.authentication.AuthorizedPartner;
import springboot.shoppingmall.partners.authentication.LoginPartner;

@RequiredArgsConstructor
@RestController
public class OrderStatusChangeController {
    private final OrderStatusChangeService orderStatusChangeService;

    @PutMapping("/orders/{orderId}/{orderItemId}/cancel")
    public ResponseEntity<OrderItemResponse> cancelOrder(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                         @PathVariable("orderId") Long orderId,
                                                         @PathVariable("orderItemId") Long orderItemId,
                                                         @RequestBody CancelRequest cancelRequest) {
        OrderItemDto orderItemDto = orderStatusChangeService.cancel(orderId, orderItemId, LocalDateTime.now(),
                cancelRequest.getCancelReason());
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/outing")
    public ResponseEntity<OrderItemResponse> outingOrder(@LoginPartner AuthorizedPartner partner,
                                                         @PathVariable("orderId") Long orderId,
                                                         @PathVariable("orderItemId") Long orderItemId) {
        OrderItemDto orderItemDto = orderStatusChangeService.outing(orderId, orderItemId);
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/finish")
    public ResponseEntity<OrderItemResponse> finishOrder(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                         @PathVariable("orderId") Long orderId,
                                                         @PathVariable("orderItemId") Long orderItemId) {
        OrderItemDto orderItemDto = orderStatusChangeService.finish(orderId, orderItemId);
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/refund")
    public ResponseEntity<OrderItemResponse> refundOrder(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                         @PathVariable("orderId") Long orderId,
                                                         @PathVariable("orderItemId") Long orderItemId,
                                                         @RequestBody OrderRefundRequest refundRequest) {

        OrderItemDto orderItemDto = orderStatusChangeService.refund(orderId, orderItemId, LocalDateTime.now(),
                refundRequest.getRefundReason());
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/exchange")
    public ResponseEntity<OrderItemResponse> exchangeOrder(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                           @PathVariable("orderId") Long orderId,
                                                           @PathVariable("orderItemId") Long orderItemId,
                                                           @RequestBody OrderExchangeRequest exchangeRequest) {

        OrderItemDto orderItemDto = orderStatusChangeService.exchange(orderId, orderItemId, LocalDateTime.now(),
                exchangeRequest.getExchangeReason());
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/checking")
    public ResponseEntity<OrderItemResponse> checkingOrder(@LoginPartner AuthorizedPartner partner,
                                                           @PathVariable("orderId") Long orderId,
                                                           @PathVariable("orderItemId") Long orderItemId) {
        OrderItemDto orderItemDto = orderStatusChangeService.checking(orderId, orderItemId);
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderId}/{orderItemId}/refund-end")
    public ResponseEntity<OrderItemResponse> refundEndOrder(@LoginPartner AuthorizedPartner partner,
                                                            @PathVariable("orderId") Long orderId,
                                                            @PathVariable("orderItemId") Long orderItemId) {
        OrderItemDto orderItemDto = orderStatusChangeService.refundEnd(orderId, orderItemId);
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }
}
