package springboot.shoppingmall.product.dto;

import java.util.List;
import java.util.stream.Collectors;
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
    private List<ProductQnaResponse> qnas;
    private List<ProductReviewResponse> reviews;

    public static ProductResponse of(Product product){
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                CategoryResponse.of(product.getCategory()), CategoryResponse.of(product.getSubCategory()),
                product.getPartnerId(), null, product.getThumbnail(), product.getDetail(),
                null, null
        );
    }

    public static ProductResponse of(Product product, Provider provider){
        List<ProductQnaResponse> qnaResponses = product.getQna().stream()
                .map(ProductQnaResponse::of)
                .collect(Collectors.toList());
        List<ProductReviewResponse> reviewResponses = product.getReviews().getReviews().stream()
                .map(ProductReviewResponse::of)
                .collect(Collectors.toList());
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getCount(),
                CategoryResponse.of(product.getCategory()), CategoryResponse.of(product.getSubCategory()),
                provider.getId(), provider.getName(), product.getThumbnail(), product.getDetail(),
                qnaResponses, reviewResponses
        );
    }
}
