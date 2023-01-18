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
public class ProductQnaResponse {
    private Long id;
    private String content;
    private String writerName;
    private String writeDate;

    private ProductQnaAnswerResponse answer;

    public ProductQnaResponse(Long id, String content, String writerName, String writeDate,
                              ProductQnaAnswerResponse answer) {
        this.id = id;
        this.content = content;
        this.writerName = writerName;
        this.writeDate = writeDate;
        this.answer = answer;
    }

    public static ProductQnaResponse of(ProductQna productQna) {
        return new ProductQnaResponse(productQna.getId(), productQna.getContent(),
                MaskingUtil.maskString(productQna.getWriter().getUserName()),
                productQna.getWriteDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                ProductQnaAnswerResponse.of(productQna.getAnswer()));
    }
}
