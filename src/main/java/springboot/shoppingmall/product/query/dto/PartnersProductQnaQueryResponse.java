package springboot.shoppingmall.product.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.utils.DateUtils;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersProductQnaQueryResponse {
    private Long id;
    private String content;
    private String writerLoginId;
    private Long productId;
    private String productName;
    private String imgFileName;
    private String writeDate;
    private boolean isAnswered;

    public static PartnersProductQnaQueryResponse of(PartnersProductQnaDto dto) {
        return new PartnersProductQnaQueryResponse(
                dto.getId(), dto.getContent(), dto.getWriterLoginId(),
                dto.getProductId(), dto.getProductName(), dto.getImgFileName(),
                DateUtils.toStringOfLocalDateTIme(dto.getWriteDate()) , dto.isAnswered()
        );
    }
}
