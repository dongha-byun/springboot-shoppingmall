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
    private int count;
    private CategoryResponse category;
    private CategoryResponse subCategory;
    private Long partnerId;
    private String partnersName;
    private String thumbnail;
    private String detail;
    private List<ProductQnaResponse> qnas;
    private List<ProductReviewResponse> reviews;

    public static ProductResponse of(Product product){
        return new ProductResponse(product.getId(), product.getProductCode(),
                product.getName(), product.getPrice(), product.getCount(),
                CategoryResponse.of(product.getCategory()), CategoryResponse.of(product.getSubCategory()),
                product.getPartnerId(), null, product.getThumbnail(), product.getDetail(),
                null, null
        );
    }

    public static ProductResponse of(ProductDto dto) {
        CategoryResponse category = CategoryResponse.of(dto.getCategory());
        CategoryResponse subCategory = CategoryResponse.of(dto.getSubCategory());

        return new ProductResponse(
                dto.getId(), dto.getProductCode(), dto.getName(),
                dto.getPrice(), dto.getCount(), category, subCategory,
                dto.getPartnersId(), null, dto.getStoredThumbnailName(), dto.getDetail(),
                null, null
        );
    }

    public static ProductResponse of(Product product, Partner partner){
        List<ProductQnaResponse> qnaResponses = product.getQna().stream()
                .map(ProductQnaResponse::of)
                .collect(Collectors.toList());
        List<ProductReviewResponse> reviewResponses = product.getReviews().getReviews().stream()
                .map(ProductReviewResponse::of)
                .collect(Collectors.toList());

        return new ProductResponse(product.getId(), product.getProductCode(),
                product.getName(), product.getPrice(), product.getCount(),
                CategoryResponse.of(product.getCategory()), CategoryResponse.of(product.getSubCategory()),
                partner.getId(), partner.getName(), product.getThumbnail(), product.getDetail(),
                qnaResponses, reviewResponses
        );
    }
}
