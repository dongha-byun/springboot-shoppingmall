package springboot.shoppingmall.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.DeliveryRepository;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        User user = findUserById(userId);
        Product product = findProductById(orderRequest.getProductId());
        Delivery delivery = getDeliveryById(orderRequest.getDeliveryId());

        Order newOrder = orderRepository.save(Order.createOrder(user, product, orderRequest.getQuantity(), delivery));

        return OrderResponse.of(newOrder);
    }

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId, String changeStatus){
        Order order = findOrderById(orderId);
        order.changeStatus(OrderStatus.valueOf(changeStatus));

        return OrderResponse.of(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        order.cancel();

        return OrderResponse.of(order);
    }

    @Transactional
    public OrderResponse outingOrder(Long orderId) {
        Order order = findOrderById(orderId);
        order.outing();

        return OrderResponse.of(order);
    }

    @Transactional
    public OrderResponse deliveryOrder(Long orderId) {
        Order order = findOrderById(orderId);
        order.delivery();

        return OrderResponse.of(order);
    }

    private Delivery getDeliveryById(Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new IllegalArgumentException("주문이 존재하지 않습니다.")
                );
    }
}
