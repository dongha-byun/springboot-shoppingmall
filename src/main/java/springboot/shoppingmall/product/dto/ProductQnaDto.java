package springboot.shoppingmall.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductQnaDto {
    private Long id;
    private String content;
    private LocalDateTime writeDate;
    private String userName;

    private ProductQnaAnswerDto answer;

    @QueryProjection
    public ProductQnaDto(Long id, String content, LocalDateTime writeDate, String userName, ProductQnaAnswerDto answer) {
        this.id = id;
        this.content = content;
        this.writeDate = writeDate;
        this.userName = userName;
        this.answer = answer;
    }
}
