package springboot.shoppingmall.product.application.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.application.dto.ProductQnaAnswerDto;

@NoArgsConstructor
@Getter
public class ProductQnaDto {
    private Long id;
    private String content;
    private LocalDateTime writeDate;
    private ProductQnaAnswerDto answer;

    @QueryProjection
    public ProductQnaDto(Long id, String content, LocalDateTime writeDate, ProductQnaAnswerDto answer) {
        this.id = id;
        this.content = content;
        this.writeDate = writeDate;
        this.answer = answer;
    }
}
