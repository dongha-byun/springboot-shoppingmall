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
    private String writerLoginId;

    private ProductQnaAnswerDto answer;

    @QueryProjection
    public ProductQnaDto(Long id, String content, LocalDateTime writeDate, String writerLoginId, ProductQnaAnswerDto answer) {
        this.id = id;
        this.content = content;
        this.writeDate = writeDate;
        this.writerLoginId = writerLoginId;
        this.answer = answer;
    }
}
