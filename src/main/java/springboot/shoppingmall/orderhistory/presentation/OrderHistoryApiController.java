package springboot.shoppingmall.orderhistory.presentation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.orderhistory.application.dto.OrderHistoryDto;
import springboot.shoppingmall.orderhistory.presentation.response.OrderHistoryResponse;
import springboot.shoppingmall.orderhistory.application.OrderHistoryService;
import springboot.shoppingmall.utils.DateUtils;

@RequiredArgsConstructor
@RestController
public class OrderHistoryApiController {

    private final OrderHistoryService orderHistoryService;

    @GetMapping("/user/orders")
    public ResponseEntity<List<OrderHistoryResponse>> getOrderHistories(
            @GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate)
    {
        LocalDateTime startDateTime = DateUtils.getLocalDateTime(startDate, 0, 0, 0);
        LocalDateTime endDateTime = DateUtils.getLocalDateTime(endDate, 23, 59, 59);

        List<OrderHistoryDto> orderHistory =
                orderHistoryService.findOrderHistory(gatewayAuthInfo.getUserId(), startDateTime, endDateTime);

        List<OrderHistoryResponse> responses = orderHistory.stream()
                .map(OrderHistoryResponse::to)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }
}
