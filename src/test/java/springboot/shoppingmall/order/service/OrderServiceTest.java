package springboot.shoppingmall.order.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryRepository;
import springboot.shoppingmall.order.domain.OrderStatus;
import springboot.shoppingmall.order.dto.OrderRequest;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.domain.ProductRepository;
import springboot.shoppingmall.user.domain.Delivery;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.domain.UserRepository;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    User user;
    Product product;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void beforeEach(){
        user = User.builder()
                .userName("테스터1").loginId("test1").password("test1!").telNo("010-0000-0000")
                .build();

        user.addDelivery(Delivery.builder()
                .nickName("수령지 1").receiverName("수령인 1").zipCode("10010")
                .address("서울시 동작구 사당동").detailAddress("101호").requestMessage("도착 시 연락주세요.").build());

        userRepository.save(user);

        Category category = categoryRepository.save(new Category("상위 1"));
        Category subCategory = categoryRepository.save(new Category("하위 1").changeParent(category));
        product = productRepository.save(new Product("상품 1", 22000, 10, category, subCategory));
    }

    @Test
    @DisplayName("주문 생성 테스트")
    void createTest(){
        // given
        Long deliveryId = user.getDeliveries().get(0).getId();

        // when
        OrderRequest orderRequest = new OrderRequest(product.getId(), 3, 3000, deliveryId);
        OrderResponse orderResponse = orderService.createOrder(user.getId(), orderRequest);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getOrderStatusName()).isEqualTo(OrderStatus.READY.getStatusName());
        assertThat(orderResponse.getProductName()).isEqualTo("상품 1");
        assertThat(orderResponse.getQuantity()).isEqualTo(3);
        assertThat(orderResponse.getTotalPrice()).isEqualTo(66000);
        assertThat(orderResponse.getDelivery().getReceiverName()).isEqualTo("수령인 1");
    }

}