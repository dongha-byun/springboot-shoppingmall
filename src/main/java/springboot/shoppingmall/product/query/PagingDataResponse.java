package springboot.shoppingmall.product.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PagingDataResponse<T> {

    private int totalCount;
    private T data;
}
