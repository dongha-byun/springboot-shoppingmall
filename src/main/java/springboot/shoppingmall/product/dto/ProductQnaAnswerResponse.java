package springboot.shoppingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductQnaAnswer;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaAnswerResponse {
    private Long id;
    private String content;

    public static ProductQnaAnswerResponse of(ProductQnaAnswer answer){
        return new ProductQnaAnswerResponse(answer.getId(), answer.getAnswer());
    }
}
