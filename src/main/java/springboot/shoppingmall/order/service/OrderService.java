package springboot.shoppingmall.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderFinder;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderSequence;
import springboot.shoppingmall.order.domain.OrderSequenceRepository;
import springboot.shoppingmall.order.dto.OrderDeliveryInvoiceResponse;
import springboot.shoppingmall.order.dto.OrderItemRequest;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.pay.domain.PayHistory;
import springboot.shoppingmall.pay.domain.PayHistoryRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductFinder;
import springboot.shoppingmall.utils.DateUtils;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderFinder orderFinder;
    private final ProductFinder productFinder;
    private final OrderRepository orderRepository;
    private final PayHistoryRepository payHistoryRepository;
    private final OrderSequenceRepository orderSequenceRepository;
    private final OrderDeliveryInterfaceService orderDeliveryInterfaceService;
    private final OrderPayService orderPayService;

    @Transactional
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        List<OrderItem> items = getOrderItems(orderRequest);

        String orderCode = generateOrderCode();
        Order newOrder = orderRepository.save(
                Order.createOrder(orderCode, userId, items,
                        orderRequest.getReceiverName(), orderRequest.getZipCode(),
                        orderRequest.getAddress(), orderRequest.getDetailAddress(),
                        orderRequest.getRequestMessage())
        );

        // 주문 정보 저장 시, 결제정보도 같이 저장한다.
        payHistoryRepository.save(
                new PayHistory(
                        newOrder.getId(), orderRequest.getPayType(), orderRequest.getTid(), newOrder.getTotalPrice()
                )
        );

        // 상품 주문이 완료되면, 상품의 재고 수를 변경한다.
        newOrder.removeQuantity();

        return OrderResponse.of(newOrder);
    }

    private List<OrderItem> getOrderItems(OrderRequest orderRequest) {
        List<OrderItem> items = new ArrayList<>();
        List<OrderItemRequest> itemRequests = orderRequest.getItems();
        for (OrderItemRequest itemRequest : itemRequests) {
            Product product = productFinder.findProductById(itemRequest.getProductId());
            product.validateQuantity(itemRequest.getQuantity());

            items.add(new OrderItem(
                    product, itemRequest.getQuantity()
            ));
        }

        return items;
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

        // 주문이 취소되면, 결제취소 요청을 보낸다.
//        PayHistory payHistory = payHistoryRepository.findByOrderId(orderId)
//                .orElseThrow(
//                        () -> new IllegalArgumentException("결제 고유번호 조회 실패")
//                );
//        orderPayService.cancel(payHistory.getTid(), payHistory.getAmount());

        // 주문된 상품이 주문 취소되면, 상품의 재고 수량을 다시 증가시킨다.
        order.increaseQuantity();

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
        order.increaseSalesVolume();

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
        order.increaseQuantity();

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
