package springboot.shoppingmall.order.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.user.domain.Delivery;

@Component
public class OrderDeliveryInterfaceServiceImpl implements OrderDeliveryInterfaceService{

    private final WebClient webClient;

    @Autowired
    public OrderDeliveryInterfaceServiceImpl() {
        this.webClient = WebClient.create("http://localhost:10000");
    }

    @Override
    public OrderDeliveryInvoiceResponse createInvoiceNumber(Order order) {

        Delivery delivery = order.getDelivery();
        String receiverName = delivery.getReceiverName();
        String zipCode = delivery.getZipCode();
        String address = delivery.getAddress();
        String detailAddress = delivery.getDetailAddress();

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
