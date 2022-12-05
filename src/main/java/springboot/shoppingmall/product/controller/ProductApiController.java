package springboot.shoppingmall.product.controller;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;

@RestController
public class ProductApiController {

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest){
        ProductResponse productResponse = ProductResponse.of(ProductRequest.toProduct(productRequest));
        try {
            return ResponseEntity.created(new URI("/product/"+productResponse.getId())).body(productResponse);
        } catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
