package springboot.shoppingmall.product.query.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PartnersProductQnaDto {
    private Long id;
    private String content;
    private Long productId;
    private String productName;
    private String imgFileName;

    private LocalDateTime writeDate;
    private boolean isAnswered;
}
