package springboot.shoppingmall.product.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.service.ProductQueryService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductQueryApiController {

    private final ProductQueryService productQueryService;


    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> queryProductsBySort(@RequestParam("categoryId") Long categoryId,
                                                                     @RequestParam("subCategoryId") Long subCategoryId,
                                                                     @RequestParam("order") String orderType){
        List<ProductResponse> products = productQueryService.findProductsByCategory(categoryId, subCategoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search-products")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam("categoryId") Long categoryId,
                                                                @RequestParam("subCategoryId") Long subCategoryId,
                                                                @RequestParam("searchKeyword") String searchKeyword) {
        List<ProductResponse> products =
                productQueryService.searchProducts(categoryId, subCategoryId, searchKeyword);
        return ResponseEntity.ok(products);
    }
}
