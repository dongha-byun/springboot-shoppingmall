package springboot.shoppingmall.product.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.Product;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductQueryResponse {
    private Long id;
    private String name;
    private double score;
    private int price;

    public static ProductQueryResponse of(Product product) {
        return new ProductQueryResponse(product.getId(), product.getName(), product.getScore(), product.getPrice());
    }
}