package springboot.shoppingmall.product.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.ProductQna;
import springboot.shoppingmall.utils.DateUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ProductQnaResponse {
    private Long id;
    private String content;
    private String writeDate;

    private ProductQnaAnswerResponse answer;

    public ProductQnaResponse(Long id, String content, String writeDate,
                              ProductQnaAnswerResponse answer) {
        this.id = id;
        this.content = content;
        this.writeDate = writeDate;
        this.answer = answer;
    }

    public static ProductQnaResponse of(ProductQna productQna) {
        return new ProductQnaResponse(productQna.getId(), productQna.getContent(),
                DateUtils.toStringOfLocalDateTIme(productQna.getWriteDate()),
                ProductQnaAnswerResponse.of(productQna.getAnswer()));
    }

    public static ProductQnaResponse of(ProductQnaDto dto) {
        return new ProductQnaResponse(dto.getId(), dto.getContent(),
                DateUtils.toStringOfLocalDateTIme(dto.getWriteDate()),
                ProductQnaAnswerResponse.of(dto.getAnswer()));
    }
}
