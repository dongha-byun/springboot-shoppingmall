package springboot.shoppingmall;

import static springboot.shoppingmall.category.CategoryAcceptanceTest.카테고리_등록;
import static springboot.shoppingmall.product.ProductAcceptanceTest.상품_등록_요청;
import static springboot.shoppingmall.delivery.DeliveryAcceptanceTest.배송지_추가_요청;

import org.junit.jupiter.api.BeforeEach;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.order.dto.OrderItemResponse;
import springboot.shoppingmall.order.dto.OrderResponse;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.delivery.presentation.response.DeliveryResponse;

public class AcceptanceProductTest extends AcceptanceTest{

    protected ProductResponse 상품;
    protected ProductResponse 상품2;
    protected DeliveryResponse 배송지;

    @BeforeEach
    public void acceptance_product_beforeEach() {
        super.acceptance_beforeEach();

        CategoryResponse 상위_카테고리 = 카테고리_등록("상위 카테고리", null).as(CategoryResponse.class);
        CategoryResponse 하위_카테고리 = 카테고리_등록("하위 카테고리", 상위_카테고리.getId()).as(CategoryResponse.class);
        상품 = 상품_등록_요청("상품 1", 10000, 200, 상위_카테고리.getId(), 하위_카테고리.getId()).as(ProductResponse.class);
        상품2 = 상품_등록_요청("상품 2", 11000, 100, 상위_카테고리.getId(), 하위_카테고리.getId()).as(ProductResponse.class);
        배송지 = 배송지_추가_요청("배송지 1",
                "수령인 1",
                "010-2345-2345",
                "10010",
                "서울시 서초구 서초동 103번지",
                "109호",
                "부재 시, 경비실에 놔주세요.").as(DeliveryResponse.class);
    }

    protected OrderItemResponse 첫_번째_주문_상품(OrderResponse orderResponse) {
        return orderResponse.getItems().get(0);
    }
}
