package springboot.shoppingmall.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
