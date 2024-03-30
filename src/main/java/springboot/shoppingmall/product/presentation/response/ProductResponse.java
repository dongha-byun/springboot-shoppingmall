package springboot.shoppingmall.product.presentation.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.product.domain.Product;
import springboot.shoppingmall.category.dto.CategoryResponse;
import springboot.shoppingmall.product.application.dto.ProductDto;
import springboot.shoppingmall.partners.domain.Partner;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String productCode;
    private String name;
    private int price;
    private int stock;
    private Long categoryId;
    private Long subCategoryId;
    private Long partnerId;
    private String partnersName;
    private String thumbnail;
    private String detail;
    private List<ProductQnaResponse> qnas;
    private List<ProductReviewResponse> reviews;

    public static ProductResponse of(Product product){
        return new ProductResponse(product.getId(), product.getProductCode(),
                product.getName(), product.getPrice(), product.getStock(),
                product.getCategoryId(), product.getSubCategoryId(),
                product.getPartnerId(), null, product.getThumbnail(), product.getDetail(),
                null, null
        );
    }

    public static ProductResponse of(ProductDto dto) {
        return new ProductResponse(
                dto.getId(), dto.getProductCode(), dto.getName(),
                dto.getPrice(), dto.getStock(), dto.getCategoryId(), dto.getSubCategoryId(),
                dto.getPartnersId(), null, dto.getStoredThumbnailName(), dto.getDetail(),
                null, null
        );
    }

}
