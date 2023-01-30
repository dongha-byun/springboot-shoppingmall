package springboot.shoppingmall.product.query;

import lombok.Getter;

@Getter
public enum ProductQueryOrderType {
    SELL("판매량 높은순"),
    SCORE("평점 높은순"),
    PRICE("낮은 가격순"),
    RECENT("최신순");

    private final String orderTypeName;

    ProductQueryOrderType(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }
}
