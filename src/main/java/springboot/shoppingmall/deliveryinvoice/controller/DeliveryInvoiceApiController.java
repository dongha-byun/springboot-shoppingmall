package springboot.shoppingmall.deliveryinvoice.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.deliveryinvoice.dto.DeliveryInvoiceRequest;
import springboot.shoppingmall.deliveryinvoice.dto.DeliveryInvoiceResponse;
import springboot.shoppingmall.deliveryinvoice.service.DeliveryInvoiceService;

/**
 * 해당 컨트롤러는 택배사의 API를 모방하여 만든 로직으로,
 * 송장번호 발부 및 조회 등의 기능을 사용 시엔
 * 의존관계를 받는게 아닌, WebClient 를 통해 API 통신으로 데이터를 주고 받아야 합니다.
 */
@RequiredArgsConstructor
@RestController
public class DeliveryInvoiceApiController {

    private final DeliveryInvoiceService deliveryInvoiceService;

    @PostMapping("/delivery-invoice")
    public ResponseEntity<DeliveryInvoiceResponse> createInvoiceNumber(@RequestBody DeliveryInvoiceRequest deliveryInvoiceRequest){
        DeliveryInvoiceResponse deliveryInvoice = deliveryInvoiceService.getDeliveryInvoice(deliveryInvoiceRequest);
        return ResponseEntity.created(URI.create("/delivery-invoice/"+deliveryInvoice.getInvoiceNumber())).body(deliveryInvoice);
    }

    @PostMapping("/delivery-invoice/{invoiceNumber}/delivery-end")
    public ResponseEntity<String> changeDeliveryEnd(@PathVariable("invoiceNumber") String invoiceNumber){
        return ResponseEntity.ok(invoiceNumber);
    }
}
