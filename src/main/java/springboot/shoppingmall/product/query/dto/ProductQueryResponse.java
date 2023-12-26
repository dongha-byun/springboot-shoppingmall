package springboot.shoppingmall.product.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.Product;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductQueryResponse {
    private Long id;
    private String name;
    private double score;
    private int salesVolume;
    private int price;
    private int quantity;
    private String thumbnail;
    private Long partnersId;
    private String partnersName;

    public static ProductQueryResponse of(ProductQueryDto dto) {
        return new ProductQueryResponse(dto.getId(), dto.getName(), dto.getScore(), dto.getSalesVolume(),
                dto.getPrice(), dto.getQuantity(), dto.getStoredThumbnailName(),
                dto.getPartnersId(), dto.getPartnersName());
    }
}
