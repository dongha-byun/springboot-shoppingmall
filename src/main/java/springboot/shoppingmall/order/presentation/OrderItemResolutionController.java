package springboot.shoppingmall.order.presentation;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.order.application.OrderItemResolutionService;
import springboot.shoppingmall.order.domain.OrderItemResolutionType;

@RequiredArgsConstructor
@RestController
public class OrderItemResolutionController {
    private final OrderItemResolutionService service;

    @PostMapping("/orders/item/{orderItemId}/{resolutionType}")
    public ResponseEntity<OrderItemResolutionResponse> orderItemResolution(
            @GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
            @PathVariable("orderItemId") Long orderItemId,
            @PathVariable("resolutionType") OrderItemResolutionType resolutionType,
            @RequestBody OrderItemResolutionRequest orderItemResolutionRequest) {

        service.process(
                gatewayAuthInfo.getUserId(), orderItemId, resolutionType,
                orderItemResolutionRequest.getContent()
        );

        return ResponseEntity.created(URI.create("/user/orders")).body(
                new OrderItemResolutionResponse(
                        orderItemId, resolutionType.getCompleteMessage()
                )
        );
    }
}
