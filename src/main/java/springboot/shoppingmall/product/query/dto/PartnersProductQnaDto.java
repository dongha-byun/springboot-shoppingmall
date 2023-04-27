package springboot.shoppingmall.product.query.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersProductQnaDto {
    private Long id;
    private String content;
    private String writerName;
    private LocalDateTime writeDate;
    private boolean isAnswered;
}
