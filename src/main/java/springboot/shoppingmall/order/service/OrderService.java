package springboot.shoppingmall.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.DeliveryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final UserFinder userFinder;
    private final OrderFinder orderFinder;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderDeliveryInterfaceService orderDeliveryInterfaceService;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        User user = userFinder.findUserById(userId);
        Product product = findProductById(orderRequest.getProductId());
        Delivery delivery = findDeliveryById(orderRequest.getDeliveryId());

        Order newOrder = orderRepository.save(Order.createOrder(user.getId(), product, orderRequest.getQuantity(), delivery));

        return OrderResponse.of(newOrder);
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, String changeStatus){
        Order order = orderFinder.findOrderById(orderId);
        order.changeStatus(OrderStatus.valueOf(changeStatus));

        if(order.isOuting()){
            // 여기서 송장번호 발부
            OrderDeliveryInvoiceResponse deliveryInvoice = orderDeliveryInterfaceService.createInvoiceNumber(order);
            order.changeInvoiceNumber(deliveryInvoice.getInvoiceNumber());
            //return OrderResponse.of(order, deliveryInvoice);
        }

        return OrderResponse.of(order);
    }

    // 주문취소
    @Transactional
    public OrderResponse cancel(Long orderId) {
        Order order = orderFinder.findOrderById(orderId);
        order.cancel();

        return OrderResponse.of(order);
    }

    // 출고중
    @Transactional
    public OrderResponse outing(Long orderId) {
        Order order = orderFinder.findOrderById(orderId);
        order.outing();

        OrderDeliveryInvoiceResponse deliveryInvoice = orderDeliveryInterfaceService.createInvoiceNumber(order);
        order.changeInvoiceNumber(deliveryInvoice.getInvoiceNumber());

        return OrderResponse.of(order);
    }

    // 구매확정
    @Transactional
    public OrderResponse finish(Long orderId) {
        Order order = orderFinder.findOrderById(orderId);
        order.finish();

        return OrderResponse.of(order);
    }

    // 검수중
    @Transactional
    public OrderResponse checking(Long orderId) {
        Order order = orderFinder.findOrderById(orderId);
        order.checking();

        return OrderResponse.of(order);
    }

    // 환불
    @Transactional
    public OrderResponse refund(Long orderId, String returnReason) {
        Order order = orderFinder.findOrderById(orderId);
        order.refund(returnReason);

        return OrderResponse.of(order);
    }

    // 환불 완료
    @Transactional
    public OrderResponse refundEnd(Long orderId) {
        Order order = orderFinder.findOrderById(orderId);
        order.refundEnd();

        return OrderResponse.of(order);
    }

    // 교환
    @Transactional
    public OrderResponse exchange(Long orderId, String exchangeReason) {
        Order order = orderFinder.findOrderById(orderId);
        order.exchange(exchangeReason);

        return OrderResponse.of(order);
    }


    private Delivery findDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
