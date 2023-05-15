package springboot.shoppingmall.order.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderSequence;
import springboot.shoppingmall.order.domain.OrderSequenceRepository;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.order.exception.OverQuantityException;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserFinder;
import springboot.shoppingmall.utils.DateUtils;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final UserFinder userFinder;
    private final OrderFinder orderFinder;
    private final ProductFinder productFinder;
    private final OrderRepository orderRepository;
    private final OrderSequenceRepository orderSequenceRepository;
    private final OrderDeliveryInterfaceService orderDeliveryInterfaceService;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        User user = userFinder.findUserById(userId);
        Product product = productFinder.findProductById(orderRequest.getProductId());

        if(product.getCount() < orderRequest.getQuantity()){
            throw new OverQuantityException("상품 재고 수가 부족합니다.");
        }

        String orderCode = generateOrderCode();
        Order newOrder = orderRepository.save(
                Order.createOrder(orderCode, user.getId(), product, orderRequest.getQuantity()
                , orderRequest.getReceiverName(), orderRequest.getZipCode(), orderRequest.getAddress()
                , orderRequest.getDetailAddress(), orderRequest.getRequestMessage())
        );

        // 상품 주문이 완료되면, 상품의 재고 수를 변경한다.
        product.removeCount(newOrder.getQuantity());

        return OrderResponse.of(newOrder);
    }

    private String generateOrderCode() {
        LocalDateTime now = LocalDateTime.now();
        String yyyyMMdd = DateUtils.toStringOfLocalDateTIme(now, "yyyyMMdd");
        OrderSequence orderSequence = orderSequenceRepository.findByDate(yyyyMMdd)
                .orElseGet(() -> OrderSequence.createSequence(now));
        if(orderSequence.isNew()) {
            orderSequenceRepository.save(orderSequence);
        }
        return orderSequence.generateOrderCode();
    }

    // 주문취소
    @Transactional
    public OrderResponse cancel(Long orderId, LocalDateTime cancelDate, String cancelReason) {
        Order order = orderFinder.findOrderById(orderId);
        order.cancel(cancelDate, cancelReason);

        // 주문된 상품이 주문 취소되면, 상품의 재고 수량을 다시 증가시킨다.
        Product product = order.getProduct();
        product.increaseCount(order.getQuantity());

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

        // 구매확정 시, 상품 판매 수량이 증가한다.
        Product product = order.getProduct();
        product.increaseSalesVolume(order.getQuantity());

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
    public OrderResponse refund(Long orderId, LocalDateTime refundDate, String refundReason) {
        Order order = orderFinder.findOrderById(orderId);
        order.refund(refundDate, refundReason);

        return OrderResponse.of(order);
    }

    // 환불 완료
    @Transactional
    public OrderResponse refundEnd(Long orderId) {
        Order order = orderFinder.findOrderById(orderId);
        order.refundEnd();

        // 환불 시, 주문 수량 만큼 수량을 증가시킨다.
        Product product = order.getProduct();
        product.increaseCount(order.getQuantity());

        return OrderResponse.of(order);
    }

    // 교환
    @Transactional
    public OrderResponse exchange(Long orderId, LocalDateTime exchangeDate, String exchangeReason) {
        Order order = orderFinder.findOrderById(orderId);
        order.exchange(exchangeDate, exchangeReason);

        return OrderResponse.of(order);
    }

}
