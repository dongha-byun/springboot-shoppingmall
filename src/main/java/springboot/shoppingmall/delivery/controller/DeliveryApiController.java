package springboot.shoppingmall.delivery.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.delivery.dto.DeliveryInvoiceRequest;
import springboot.shoppingmall.delivery.dto.DeliveryInvoiceResponse;
import springboot.shoppingmall.delivery.service.DeliveryInvoiceService;

@RequiredArgsConstructor
@RestController
public class DeliveryApiController {
    private final DeliveryInvoiceService deliveryInvoiceService;

    @PostMapping("/delivery-invoice")
    public ResponseEntity<DeliveryInvoiceResponse> createInvoiceNumber(@RequestBody DeliveryInvoiceRequest deliveryInvoiceRequest){
        DeliveryInvoiceResponse deliveryInvoice = deliveryInvoiceService.getDeliveryInvoice(deliveryInvoiceRequest);
        return ResponseEntity.created(URI.create("/delivery-invoice/"+deliveryInvoice.getInvoiceNumber())).body(deliveryInvoice);
    }
}
