package springboot.shoppingmall;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.service.OrderDeliveryInterfaceService;
import springboot.shoppingmall.user.domain.Delivery;

@TestConfiguration
public class TestOrderConfig {

    @Bean
    public OrderDeliveryInterfaceService orderDeliveryInterfaceService() {
        return new OrderDeliveryInterfaceService() {
            @Override
            public OrderDeliveryInvoiceResponse createInvoiceNumber(Order order) {
                Delivery delivery = order.getDelivery();
                return new OrderDeliveryInvoiceResponse(
                        "test-delivery-invoice-number",
                        delivery.getReceiverName(),
                        delivery.getZipCode(),
                        delivery.getAddress(),
                        delivery.getDetailAddress(),
                        "판매자 1",
                        "00990",
                        "판매자 주소",
                        "판매자 상세주소"
                );
            }
        };
    }
}
