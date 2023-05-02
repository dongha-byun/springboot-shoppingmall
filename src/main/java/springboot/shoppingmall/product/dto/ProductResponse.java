package springboot.shoppingmall.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.providers.domain.Provider;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private int count;
    private CategoryResponse category;
    private CategoryResponse subCategory;
    private Long partnerId;
    private String partnersName;
    private String thumbnail;
    private String detail;

    public static ProductResponse of(Product product){
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount()
                , CategoryResponse.of(product.getCategory()), CategoryResponse.of(product.getSubCategory())
                , product.getPartnerId(), null, product.getThumbnail(), product.getDetail()
        );
    }

    public static ProductResponse of(Product product, Provider provider){
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount()
                , CategoryResponse.of(product.getCategory()), CategoryResponse.of(product.getSubCategory())
                , provider.getId(), provider.getName(), product.getThumbnail(), product.getDetail()
        );
    }
}
