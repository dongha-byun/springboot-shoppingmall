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
        if(answer == null || answer.getId() == null){
            return new ProductQnaAnswerResponse();
        }
        return new ProductQnaAnswerResponse(answer.getId(), answer.getAnswer());
    }
}
