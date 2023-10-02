package springboot.shoppingmall.order.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.service.OrderService;
import springboot.shoppingmall.order.service.dto.OrderCreateDto;
import springboot.shoppingmall.order.service.dto.OrderDto;

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
        OrderCreateDto orderCreateDto = orderRequest.toDto();
        OrderDto orderDto = orderService.createOrder(user.getId(), orderCreateDto);
        OrderResponse response = OrderResponse.of(orderDto);

        return ResponseEntity.created(URI.create("/orders/" + response.getId())).body(response);
    }
}
