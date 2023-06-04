package springboot.shoppingmall.product.dto;


import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductReviewDto {
    private Long id;
    private String content;
    private int score;
    private LocalDateTime writeDate;
    private String writerLoginId;

    @QueryProjection
    public ProductReviewDto(Long id, String content, int score, LocalDateTime writeDate, String writerLoginId) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.writeDate = writeDate;
        this.writerLoginId = writerLoginId;
    }
}
