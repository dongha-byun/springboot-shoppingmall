package springboot.shoppingmall.product.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.product.dto.ProductDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductQueryResponse {
    private Long id;
    private String name;
    private double score;
    private int price;
    private int count;
    private String thumbnail;
    private String partnerName;

    public static ProductQueryResponse of(Product product) {
        return new ProductQueryResponse(product.getId(), product.getName(), product.getScore(),
                product.getPrice(), product.getCount(), product.getThumbnail(), null);
    }

    public static ProductQueryResponse of(ProductQueryDto dto) {
        return new ProductQueryResponse(dto.getId(), dto.getName(), dto.getScore(),
                dto.getPrice(), dto.getQuantity(), dto.getStoredThumbnailName(),
                dto.getPartnerName());
    }
}
