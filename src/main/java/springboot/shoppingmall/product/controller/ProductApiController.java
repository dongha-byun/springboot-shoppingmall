package springboot.shoppingmall.product.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.service.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@AuthenticationStrategy AuthorizedUser authorizedUser,
                                                         @Validated @RequestBody ProductRequest productRequest,
                                                         BindingResult bindingResult){

        ProductResponse productResponse = productService.saveProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/"+productResponse.getId())).body(productResponse);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Long id){
        ProductResponse productResponse = productService.findProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam("categoryId") Long categoryId,
                                                             @RequestParam("subCategoryId") Long subCategoryId){
        List<ProductResponse> products = productService.findProductsByCategory(categoryId, subCategoryId);
        return ResponseEntity.ok(products);
    }
}
