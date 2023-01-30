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

//    @GetMapping("/products")
//    public ResponseEntity<List<ProductResponse>> findAllProducts(@RequestParam("categoryId") Long categoryId,
//                                                                 @RequestParam("subCategoryId") Long subCategoryId){
//        List<ProductResponse> products = productQueryService.findProductsByCategory(categoryId, subCategoryId);
//        return ResponseEntity.ok(products);
//    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> queryProductsBySort(@RequestParam("categoryId") Long categoryId,
                                                                     @RequestParam("subCategoryId") Long subCategoryId,
                                                                     @RequestParam("order") String orderType){
        log.info("{} : {} : {}", categoryId, subCategoryId, orderType);
        List<ProductResponse> products = productQueryService.findProductsByCategory(categoryId, subCategoryId);
        return ResponseEntity.ok(products);
    }
}
