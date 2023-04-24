package springboot.shoppingmall;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.service.OrderDeliveryInterfaceService;

@TestConfiguration
public class TestOrderConfig {

    @Bean
    public OrderDeliveryInterfaceService orderDeliveryInterfaceService() {
        return order -> new OrderDeliveryInvoiceResponse(
                "test-delivery-invoice-number",
                order.getReceiverName(),
                order.getZipCode(),
                order.getAddress(),
                order.getDetailAddress(),
                "판매자 1",
                "00990",
                "판매자 주소",
                "판매자 상세주소"
        );
    }
}
