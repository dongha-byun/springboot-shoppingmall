package springboot.shoppingmall.product.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PagingDataResponse<T> {

    private Integer totalCount;
    private String categoryName;
    private String subCategoryName;
    private T data;

    public PagingDataResponse(T data) {
        this.data = data;
    }

    public PagingDataResponse(Integer totalCount, T data) {
        this.totalCount = totalCount;
        this.data = data;
    }
}
