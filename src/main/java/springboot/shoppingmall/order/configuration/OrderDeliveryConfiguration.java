package springboot.shoppingmall.order.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.shoppingmall.order.service.OrderDeliveryInterfaceService;
import springboot.shoppingmall.order.service.OrderDeliveryInterfaceServiceImpl;

//@Configuration
public class OrderDeliveryConfiguration {

    @Bean
    public OrderDeliveryInterfaceService orderDeliveryInterfaceService() {
        return new OrderDeliveryInterfaceServiceImpl();
    }
}
