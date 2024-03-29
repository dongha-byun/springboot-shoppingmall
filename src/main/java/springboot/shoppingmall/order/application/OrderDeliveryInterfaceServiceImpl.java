package springboot.shoppingmall.order.application;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;

@Component
public class OrderDeliveryInterfaceServiceImpl implements OrderDeliveryInterfaceService{

    private final WebClient webClient;
    public OrderDeliveryInterfaceServiceImpl() {
        this.webClient = WebClient.create("http://localhost:10010");
    }

    @Override
    public OrderDeliveryInvoiceResponse createInvoiceNumber(Order order) {
        OrderDeliveryInfo orderDeliveryInfo = order.getOrderDeliveryInfo();
        String receiverName = orderDeliveryInfo.getReceiverName();
        String zipCode = orderDeliveryInfo.getZipCode();
        String address = orderDeliveryInfo.getAddress();
        String detailAddress = orderDeliveryInfo.getDetailAddress();

        Map<String, String> params = new HashMap<>();
        params.put("receiverName", receiverName);
        params.put("receiverZipcode", zipCode);
        params.put("receiverAddress", address);
        params.put("receiverDetailAddress", detailAddress);

        return webClient.post()
                .uri("/delivery-invoice")
                .body(Mono.just(params), Map.class)
                .retrieve()
                .bodyToMono(OrderDeliveryInvoiceResponse.class)
                .block();
    }
}
