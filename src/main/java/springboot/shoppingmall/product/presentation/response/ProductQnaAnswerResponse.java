package springboot.shoppingmall.product.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductQnaAnswer;
import springboot.shoppingmall.product.application.dto.ProductQnaAnswerDto;
import springboot.shoppingmall.utils.DateUtils;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductQnaAnswerResponse {
    private Long id;
    private String answer;
    private String answerDate;

    public static ProductQnaAnswerResponse of(ProductQnaAnswer answer){
        if(answer == null || answer.getId() == null){
            return new ProductQnaAnswerResponse();
        }
        return new ProductQnaAnswerResponse(answer.getId(), answer.getAnswer(),
                DateUtils.toStringOfLocalDateTIme(answer.getAnswerDate()));
    }

    public static ProductQnaAnswerResponse of(ProductQnaAnswerDto dto) {
        if(dto == null) {
            return null;
        }
        return new ProductQnaAnswerResponse(dto.getId(), dto.getAnswer(),
                DateUtils.toStringOfLocalDateTIme(dto.getAnswerDate()));
    }

}
