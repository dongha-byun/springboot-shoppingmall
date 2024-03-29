package springboot.shoppingmall;

import java.util.UUID;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.application.OrderDeliveryInterfaceService;

@TestConfiguration
public class TestOrderConfig {

    @Bean
    public OrderDeliveryInterfaceService orderDeliveryInterfaceService() {
        return order -> new OrderDeliveryInvoiceResponse(
                UUID.randomUUID().toString().substring(0, 10),
                order.getOrderDeliveryInfo().getReceiverName(),
                order.getOrderDeliveryInfo().getZipCode(),
                order.getOrderDeliveryInfo().getAddress(),
                order.getOrderDeliveryInfo().getDetailAddress(),
                "판매자 1",
                "00990",
                "판매자 주소",
                "판매자 상세주소"
        );
    }
}
