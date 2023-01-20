package springboot.shoppingmall.delivery.service;

import springboot.shoppingmall.delivery.dto.DeliveryInvoiceRequest;
import springboot.shoppingmall.delivery.dto.DeliveryInvoiceResponse;

public interface DeliveryInvoiceService {

    DeliveryInvoiceResponse getDeliveryInvoice(DeliveryInvoiceRequest deliveryInvoiceRequest);
}
