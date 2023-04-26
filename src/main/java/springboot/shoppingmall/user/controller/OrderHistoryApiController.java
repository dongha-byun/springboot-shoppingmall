package springboot.shoppingmall.user.controller;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.user.dto.OrderHistoryDto;
import springboot.shoppingmall.user.service.OrderHistoryService;
import springboot.shoppingmall.utils.DateUtils;

@RequiredArgsConstructor
@RestController
public class OrderHistoryApiController {

    private final OrderHistoryService orderHistoryService;

    @GetMapping("/user/orders")
    public ResponseEntity<List<OrderHistoryDto>> getOrderHistories(@AuthenticationStrategy AuthorizedUser user,
                                                                   @RequestParam(name = "startDate") String startDate,
                                                                   @RequestParam(name = "endDate") String endDate){
        LocalDateTime startDateTime = DateUtils.getLocalDateTime(startDate, 0, 0, 0);
        LocalDateTime endDateTime = DateUtils.getLocalDateTime(endDate, 23, 59, 59);

        List<OrderHistoryDto> orderHistory = orderHistoryService.findOrderHistory(user.getId(), startDateTime,
                endDateTime);
        return ResponseEntity.ok().body(orderHistory);
    }
}
