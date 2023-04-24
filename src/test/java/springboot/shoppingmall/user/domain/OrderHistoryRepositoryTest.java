package springboot.shoppingmall.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.domain.Order;
import springboot.shoppingmall.order.domain.OrderRepository;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.dto.OrderHistoryDto;

@Transactional
@SpringBootTest
class OrderHistoryRepositoryTest {

    @Autowired
    OrderHistoryRepository orderHistoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    OrderRepository orderRepository;

    @Commit
    @Test
    @DisplayName("사용자 별 주문내역 조회")
    void orderHistoryTest(){
        // given
        User user = userRepository.save(User.builder().userName("테스터")
                .loginId("test1")
                .password("test1!")
                .telNo("010-1234-1234")
                .build());
        Delivery delivery = deliveryRepository.save(Delivery.builder().user(user)
                .address("주소지 1")
                .detailAddress("상세 주소지 1")
                .nickName("배송지 1")
                .receiverName("수령인 1")
                .requestMessage("요구사항 1")
                .zipCode("10001")
                .build());
        Category parent = categoryRepository.save(new Category("카테고리1"));
        Category child = categoryRepository.save(new Category("하위 카테고리1"));

        Product product = productRepository.save(Product.builder().name("상품1")
                .category(parent)
                .subCategory(child)
                .price(10000)
                .count(22)
                .build());

        Order order1 = Order.createOrder(user.getId(), product, 2, delivery.getReceiverName()
                , delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage());
        Order order2 = Order.createOrder(user.getId(), product, 3, delivery.getReceiverName()
                , delivery.getZipCode(), delivery.getAddress(), delivery.getDetailAddress()
                , delivery.getRequestMessage());
        orderRepository.save(order1);
        orderRepository.save(order2);

        // when
        List<OrderHistoryDto> orderHistories = orderHistoryRepository.queryOrderHistory(user);

        // then
        assertThat(orderHistories).hasSize(2);
    }
}