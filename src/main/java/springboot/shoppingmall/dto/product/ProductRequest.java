package springboot.shoppingmall.dto.product;

import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    private int price;
}
