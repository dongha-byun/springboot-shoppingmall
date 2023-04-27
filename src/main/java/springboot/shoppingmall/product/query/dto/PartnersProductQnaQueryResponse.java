package springboot.shoppingmall.product.query.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnersProductQnaQueryResponse {
    private Long id;
    private String content;
    private String writerName;
    private LocalDateTime writeDate;
    private boolean isAnswered;

    public static PartnersProductQnaQueryResponse of(PartnersProductQnaDto dto) {
        return new PartnersProductQnaQueryResponse(
                dto.getId(), dto.getContent(), dto.getWriterName(),
                dto.getWriteDate(), dto.isAnswered()
        );
    }
}
