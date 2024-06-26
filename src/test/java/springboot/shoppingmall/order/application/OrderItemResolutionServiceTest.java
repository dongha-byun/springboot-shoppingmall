package springboot.shoppingmall.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isA;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import springboot.shoppingmall.IntegrationTest;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.client.couponservice.CouponServiceClient;
import springboot.shoppingmall.client.payservice.PayServiceClient;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderDeliveryInfo;
import springboot.shoppingmall.order.domain.OrderItem;
import springboot.shoppingmall.order.domain.OrderItemResolutionHistory;
import springboot.shoppingmall.order.domain.OrderItemResolutionHistoryRepository;
import springboot.shoppingmall.order.domain.OrderItemResolutionType;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.partners.domain.Partner;
import springboot.shoppingmall.partners.domain.PartnerRepository;
import springboot.shoppingmall.pay.domain.PayHistory;
import springboot.shoppingmall.pay.domain.PayHistoryRepository;
import springboot.shoppingmall.payment.domain.PayType;
import springboot.shoppingmall.product.domain.Product;

class OrderItemResolutionServiceTest extends IntegrationTest {

    @Autowired
    OrderItemResolutionService service;

    @MockBean
    PayServiceClient payServiceClient;

    @MockBean
    CouponServiceClient couponServiceClient;

    @Autowired
    OrderItemResolutionHistoryRepository orderItemResolutionHistoryRepository;

    @Autowired
    PayHistoryRepository payHistoryRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PartnerRepository partnerRepository;

    Category category;
    Category subCategory;
    Product product;
    Partner partner;

    OrderDeliveryInfo orderDeliveryInfo;
    LocalDateTime resolutionDateTime;

    Long userId = 100L;
    String orderCode = "order-code-1";


    @BeforeEach
    void beforeEach() {
        partner = partnerRepository.save(
                new Partner("테스트판매사", "테스트대표", "테스트시 테스트구 테스트동", "031-444-1234", "110-22-334411",
                        "test_partner", "test_partner1!", true)
        );
        category = categoryRepository.save(new Category("상위 카테고리"));
        subCategory = categoryRepository.save(new Category("하위 카테고리").changeParent(category));

        LocalDateTime now = LocalDateTime.of(2023, 8, 15, 12, 12, 12);
        product = saveProduct("슬랙스", 23100, 10, 2.7, 7, category.getId(), subCategory.getId(), partner.getId(), now);

        orderDeliveryInfo = new OrderDeliveryInfo(
                "수령인 1", "010-2345-2345",
                "10010", "서울시 테스트구 임시동", "목타워 555호",
                "문 앞에 놔주세요."
        );

        resolutionDateTime = LocalDateTime.of(2023, 11, 21, 17, 6, 22);
    }

    @Test
    @DisplayName("상품의 주문을 취소한다.")
    void save_cancel_history() {
        // given
        OrderItem orderItem = new OrderItem(product, 3, OrderStatus.PREPARED);
        Order order = Order.createOrder(
                orderCode, userId, List.of(orderItem), orderDeliveryInfo
        );
        orderRepository.save(order);

        payHistoryRepository.save(
                new PayHistory(
                        order.getId(), PayType.KAKAO_PAY, "created-transaction-id", order.getRealPayPrice()
                )
        );

        doNothing().when(payServiceClient).cancel(isA(Long.class), isA(Integer.class));
        doNothing().when(couponServiceClient).recoveryCoupon(isA(Long.class));

        // when
        Long historyId = service.saveResolutionHistory(
                userId, orderItem.getId(), OrderItemResolutionType.CANCEL,
                resolutionDateTime,
                "취소합니다."
        );

        // then
        OrderItemResolutionHistory savedHistory = orderItemResolutionHistoryRepository.findById(historyId)
                .orElseThrow();

        assertThat(savedHistory.getId()).isEqualTo(historyId);
        assertThat(savedHistory.getReason()).isEqualTo("취소합니다.");
        assertThat(savedHistory.getResolutionType()).isEqualTo(OrderItemResolutionType.CANCEL);
    }

    @Test
    @DisplayName("사유를 작성하지 않으면, 주문을 취소할 수 없다.")
    void save_cancel_history_fail_with_no_content() {
        // given
        OrderItem orderItem = new OrderItem(product, 3, OrderStatus.PREPARED);
        Order order = Order.createOrder(
                orderCode, userId, List.of(orderItem), orderDeliveryInfo
        );
        orderRepository.save(order);

        // when
        assertThatThrownBy(
                () -> service.saveResolutionHistory(
                        userId, orderItem.getId(), OrderItemResolutionType.CANCEL, resolutionDateTime, ""
                )
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("주문 상품의 환불을 요청한다.")
    void save_refund_history() {
        // given
        OrderItem orderItem = new OrderItem(product, 3, OrderStatus.DELIVERY_END);
        Order order = Order.createOrder(
                orderCode, userId, List.of(orderItem), orderDeliveryInfo
        );
        orderRepository.save(order);

        // when
        Long historyId = service.saveResolutionHistory(
                userId, orderItem.getId(), OrderItemResolutionType.REFUND, resolutionDateTime, "환불합니다."
        );

        // then
        OrderItemResolutionHistory savedHistory = orderItemResolutionHistoryRepository.findById(historyId)
                .orElseThrow();

        assertThat(savedHistory.getId()).isEqualTo(historyId);
        assertThat(savedHistory.getReason()).isEqualTo("환불합니다.");
        assertThat(savedHistory.getResolutionType()).isEqualTo(OrderItemResolutionType.REFUND);
    }

    @Test
    @DisplayName("환불사유 입력 없이, 환불할 수 없다.")
    void save_refund_history_fail_with_no_content() {
        // given
        OrderItem orderItem = new OrderItem(product, 3, OrderStatus.DELIVERY_END);
        Order order = Order.createOrder(
                orderCode, userId, List.of(orderItem), orderDeliveryInfo
        );
        orderRepository.save(order);

        // when
        assertThatThrownBy(
                () -> service.saveResolutionHistory(
                        userId, orderItem.getId(), OrderItemResolutionType.REFUND, resolutionDateTime, ""
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상품의 교환을 요청한다.")
    void save_exchange_history() {
        // given
        OrderItem orderItem = new OrderItem(product, 3, OrderStatus.DELIVERY_END);
        Order order = Order.createOrder(
                orderCode, userId, List.of(orderItem), orderDeliveryInfo
        );
        orderRepository.save(order);

        // when
        Long historyId = service.saveResolutionHistory(
                userId, orderItem.getId(), OrderItemResolutionType.EXCHANGE, resolutionDateTime, "상품 교환 신청합니다."
        );

        // then
        OrderItemResolutionHistory savedHistory = orderItemResolutionHistoryRepository.findById(historyId)
                .orElseThrow();

        assertThat(savedHistory.getId()).isEqualTo(historyId);
        assertThat(savedHistory.getReason()).isEqualTo("상품 교환 신청합니다.");
        assertThat(savedHistory.getResolutionType()).isEqualTo(OrderItemResolutionType.EXCHANGE);
    }

    @Test
    @DisplayName("교환사유 입력 없이, 교환할 수 없다.")
    void save_exchange_history_fail_with_no_content() {
        // given
        OrderItem orderItem = new OrderItem(product, 3, OrderStatus.DELIVERY_END);
        Order order = Order.createOrder(
                orderCode, userId, List.of(orderItem), orderDeliveryInfo
        );
        orderRepository.save(order);

        // when
        assertThatThrownBy(
                () -> service.saveResolutionHistory(
                        userId, orderItem.getId(), OrderItemResolutionType.EXCHANGE, resolutionDateTime, ""
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }
}