package springboot.shoppingmall.product.dto;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.utils.MaskingUtil;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class ProductQnaResponse {
    private Long id;
    private String content;
    private String writerName;
    private String writeDate;

    public static ProductQnaResponse of(ProductQna productQna) {
        return new ProductQnaResponse(productQna.getId(), productQna.getContent(),
                MaskingUtil.maskString(productQna.getWriter().getUserName()),
                productQna.getWriteDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));
    }
}
