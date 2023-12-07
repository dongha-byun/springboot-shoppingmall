package springboot.shoppingmall.order.presentation;

import java.net.URI;
import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.common.validation.bean.BeanValidation;
import springboot.shoppingmall.common.validation.bean.BeanValidationException;
import springboot.shoppingmall.order.application.OrderItemResolutionService;
import springboot.shoppingmall.order.domain.OrderItemResolutionType;

@RequiredArgsConstructor
@BeanValidation
@RestController
public class OrderItemResolutionController {
    private final OrderItemResolutionService service;

    @PostMapping("/orders/item/{orderItemId}/{resolutionType}")
    public ResponseEntity<OrderItemResolutionResponse> orderItemResolution(
            @GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
            @PathVariable("orderItemId") Long orderItemId,
            @PathVariable("resolutionType") OrderItemResolutionType resolutionType,
            @Valid @RequestBody OrderItemResolutionRequest orderItemResolutionRequest,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new BeanValidationException(bindingResult);
        }

        service.saveResolutionHistory(
                gatewayAuthInfo.getUserId(), orderItemId, resolutionType,
                LocalDateTime.now(), orderItemResolutionRequest.getContent()
        );

        return ResponseEntity.created(URI.create("/user/orders")).body(
                new OrderItemResolutionResponse(
                        orderItemId, resolutionType.getCompleteMessage()
                )
        );
    }
}
