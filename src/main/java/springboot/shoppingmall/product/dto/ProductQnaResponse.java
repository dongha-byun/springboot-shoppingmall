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
    private String writerLoginId;
    private String writeDate;

    private ProductQnaAnswerResponse answer;

    public ProductQnaResponse(Long id, String content, String writerLoginId, String writeDate,
                              ProductQnaAnswerResponse answer) {
        this.id = id;
        this.content = content;
        this.writerLoginId = writerLoginId;
        this.writeDate = writeDate;
        this.answer = answer;
    }

    public static ProductQnaResponse of(ProductQna productQna) {
        return new ProductQnaResponse(productQna.getId(), productQna.getContent(),
                productQna.getWriterLoginId(),
                DateUtils.toStringOfLocalDateTIme(productQna.getWriteDate()),
                ProductQnaAnswerResponse.of(productQna.getAnswer()));
    }

    public static ProductQnaResponse of(ProductQnaDto dto) {
        return new ProductQnaResponse(dto.getId(), dto.getContent(),
                dto.getWriterLoginId(),
                DateUtils.toStringOfLocalDateTIme(dto.getWriteDate()),
                ProductQnaAnswerResponse.of(dto.getAnswer()));
    }
}
