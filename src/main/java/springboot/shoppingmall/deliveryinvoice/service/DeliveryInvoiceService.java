package springboot.shoppingmall.deliveryinvoice.service;

import springboot.shoppingmall.deliveryinvoice.dto.DeliveryInvoiceRequest;
import springboot.shoppingmall.deliveryinvoice.dto.DeliveryInvoiceResponse;

public interface DeliveryInvoiceService {

    DeliveryInvoiceResponse getDeliveryInvoice(DeliveryInvoiceRequest deliveryInvoiceRequest);
    String changeDeliveryEnd(String invoiceNumber);
}
