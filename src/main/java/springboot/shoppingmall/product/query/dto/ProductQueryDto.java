package springboot.shoppingmall.product.query.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProductQueryDto {
    private Long id;
    private String name;
    private int price;
    private int quantity;
    private double score;
    private String storedThumbnailName;
    private String viewThumbnailName;
    private String partnerName;

    @QueryProjection
    public ProductQueryDto(Long id, String name, int price, int quantity, double score,
                           String storedThumbnailName, String viewThumbnailName, String partnerName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.score = score;
        this.storedThumbnailName = storedThumbnailName;
        this.viewThumbnailName = viewThumbnailName;
        this.partnerName = partnerName;
    }
}
