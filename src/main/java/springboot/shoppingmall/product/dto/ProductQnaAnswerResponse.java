package springboot.shoppingmall.product.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductQnaAnswer;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaAnswerResponse {
    private Long id;
    private String answer;
    private LocalDateTime answerDate;

    public static ProductQnaAnswerResponse of(ProductQnaAnswer answer){
        if(answer == null || answer.getId() == null){
            return new ProductQnaAnswerResponse();
        }
        return new ProductQnaAnswerResponse(answer.getId(), answer.getAnswer(), answer.getAnswerDate());
    }

    public static ProductQnaAnswerResponse of(ProductQnaAnswerDto dto) {
        return new ProductQnaAnswerResponse(dto.getId(), dto.getAnswer(), dto.getAnswerDate());
    }

}
