package springboot.shoppingmall.order.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.order.application.OrderStatusChangeService;
import springboot.shoppingmall.order.application.dto.OrderItemDto;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.partners.authentication.AuthorizedPartner;
import springboot.shoppingmall.partners.authentication.LoginPartner;

@RequiredArgsConstructor
@RestController
public class OrderStatusChangeController {
    private final OrderStatusChangeService orderStatusChangeService;

    @PutMapping("/orders/{orderItemId}/outing")
    public ResponseEntity<OrderItemResponse> outingOrder(@LoginPartner AuthorizedPartner partner,
                                                         @PathVariable("orderItemId") Long orderItemId) {
        OrderItemDto orderItemDto = orderStatusChangeService.outing(orderItemId);
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderItemId}/finish")
    public ResponseEntity<OrderItemResponse> finishOrder(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                         @PathVariable("orderItemId") Long orderItemId) {
        OrderItemDto orderItemDto = orderStatusChangeService.finish(orderItemId);
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderItemId}/checking")
    public ResponseEntity<OrderItemResponse> checkingOrder(@LoginPartner AuthorizedPartner partner,
                                                           @PathVariable("orderItemId") Long orderItemId) {
        OrderItemDto orderItemDto = orderStatusChangeService.checking(orderItemId);
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/orders/{orderItemId}/refund-end")
    public ResponseEntity<OrderItemResponse> refundEndOrder(@LoginPartner AuthorizedPartner partner,
                                                            @PathVariable("orderItemId") Long orderItemId) {
        OrderItemDto orderItemDto = orderStatusChangeService.refundEnd(orderItemId);
        OrderItemResponse response = OrderItemResponse.of(orderItemDto);
        return ResponseEntity.ok().body(response);
    }
}
