package springboot.shoppingmall.product.query.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PartnersProductQnaDto {
    private Long id;
    private String content;
    private String writerLoginId;
    private Long productId;
    private String productName;
    private String imgFileName;

    private LocalDateTime writeDate;
    private boolean isAnswered;

    @QueryProjection
    public PartnersProductQnaDto(Long id, String content, String writerLoginId, Long productId, String productName,
                                 String imgFileName, LocalDateTime writeDate, boolean isAnswered) {
        this.id = id;
        this.content = content;
        this.writerLoginId = writerLoginId;
        this.productId = productId;
        this.productName = productName;
        this.imgFileName = imgFileName;
        this.writeDate = writeDate;
        this.isAnswered = isAnswered;
    }
}
