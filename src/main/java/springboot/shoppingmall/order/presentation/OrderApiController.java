package springboot.shoppingmall.order.presentation;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.application.OrderService;
import springboot.shoppingmall.order.application.dto.OrderCreateDto;
import springboot.shoppingmall.order.application.dto.OrderDto;

/**
 * 서비스 내에서 호출되는 API 명세
 */
@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                     @RequestBody OrderRequest orderRequest){
        OrderCreateDto orderCreateDto = orderRequest.toDto();
        OrderDto orderDto = orderService.createOrder(gatewayAuthInfo.getUserId(), orderCreateDto);
        OrderResponse response = OrderResponse.of(orderDto);

        return ResponseEntity.created(URI.create("/orders/" + response.getId())).body(response);
    }
}
