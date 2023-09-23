package springboot.shoppingmall.product.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductQnaAnswer;

@NoArgsConstructor
@Getter
public class ProductQnaAnswerDto {
    private Long id;
    private String answer;
    private LocalDateTime answerDate;

    @QueryProjection
    public ProductQnaAnswerDto(Long id, String answer, LocalDateTime answerDate) {
        this.id = id;
        this.answer = answer;
        this.answerDate = answerDate;
    }

    public static ProductQnaAnswerDto of(ProductQnaAnswer answer) {
        if(answer == null) {
            return null;
        }

        return new ProductQnaAnswerDto(
                answer.getId(), answer.getAnswer(), answer.getAnswerDate()
        );
    }
}
