package springboot.shoppingmall.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.order.dto.DeliveryEndRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.service.OrderDeliveryInvoiceService;
import springboot.shoppingmall.order.service.OrderService;

/**
 * 해당 controller 는 택배사에서 배송중 / 배송완료 상태를 처리하기 위해 호출하는 API 명세
 */
@RequiredArgsConstructor
@RestController
public class OrderDeliveryInvoiceApiController {

    private final OrderDeliveryInvoiceService invoiceService;

    /**
     * 택배사에서 해당 api 를 호출해, 특정 주문의 배송상태를 배송중 으로 변경함
     */
    @PutMapping("/orders/{invoiceNumber}/delivery")
    public ResponseEntity<OrderResponse> orderDelivery(@PathVariable("invoiceNumber") String invoiceNumber) {
        OrderResponse order = invoiceService.delivery(invoiceNumber);
        return ResponseEntity.ok(order);
    }

    /**
     * 택배사에서 해당 api 를 호출해, 특정 주문의 배송상태를 배송상태 로 변경함
     */
    @PutMapping("/orders/{invoiceNumber}/delivery-end")
    public ResponseEntity<OrderResponse> orderDeliveryEnd(
            @PathVariable("invoiceNumber") String invoiceNumber,
            @RequestBody DeliveryEndRequest request
    ) {
        OrderResponse orderResponse = invoiceService.deliveryEnd(invoiceNumber, request);
        return ResponseEntity.ok(orderResponse);
    }
}
