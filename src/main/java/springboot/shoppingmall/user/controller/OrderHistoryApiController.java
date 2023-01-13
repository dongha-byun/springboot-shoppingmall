package springboot.shoppingmall.user.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.user.dto.OrderHistoryDto;
import springboot.shoppingmall.user.service.OrderHistoryService;

@RequiredArgsConstructor
@RestController
public class OrderHistoryApiController {

    private final OrderHistoryService orderHistoryService;

    @GetMapping("/user/orders")
    public ResponseEntity<List<OrderHistoryDto>> getOrderHistories(@AuthenticationStrategy AuthorizedUser user){
        List<OrderHistoryDto> orderHistory = orderHistoryService.findOrderHistory(user.getId());
        return ResponseEntity.ok().body(orderHistory);
    }
}
