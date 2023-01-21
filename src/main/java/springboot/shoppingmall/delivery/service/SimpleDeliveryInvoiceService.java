package springboot.shoppingmall.delivery.service;

import java.util.UUID;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.delivery.dto.DeliveryInvoiceRequest;
import springboot.shoppingmall.delivery.dto.DeliveryInvoiceResponse;

@Component
public class SimpleDeliveryInvoiceService implements DeliveryInvoiceService{

    @Override
    public DeliveryInvoiceResponse getDeliveryInvoice(DeliveryInvoiceRequest deliveryInvoiceRequest) {
        String invoiceNumber = UUID.randomUUID().toString();
        return DeliveryInvoiceResponse.of(invoiceNumber, deliveryInvoiceRequest);
    }

    @Override
    public String changeDeliveryEnd(String invoiceNumber) {
        return null;
    }
}
