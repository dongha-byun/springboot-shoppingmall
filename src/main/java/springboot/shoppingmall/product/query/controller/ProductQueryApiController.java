package springboot.shoppingmall.product.query.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.query.PagingDataResponse;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryResponse;
import springboot.shoppingmall.product.query.service.ProductQueryService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductQueryApiController {

    private final ProductQueryService productQueryService;


    @GetMapping("/products")
    public ResponseEntity<PagingDataResponse<List<ProductQueryResponse>>> queryProductsBySort(@RequestParam("categoryId") Long categoryId,
                                                                                             @RequestParam("subCategoryId") Long subCategoryId,
                                                                                             @RequestParam("orderType") String orderType){
        log.info("categoryId={}, subCategoryId={}, orderType={}", categoryId, subCategoryId, orderType);
        List<ProductQueryResponse> products = productQueryService.findProductByOrder(categoryId,
                subCategoryId, ProductQueryOrderType.valueOf(orderType));
        return ResponseEntity.ok(new PagingDataResponse<>(products.size(), products));
    }

    @GetMapping("/search-products")
    public ResponseEntity<List<ProductQueryResponse>> searchProducts(@RequestParam("categoryId") Long categoryId,
                                                                     @RequestParam("subCategoryId") Long subCategoryId,
                                                                     @RequestParam("searchKeyword") String searchKeyword) {
        List<ProductQueryResponse> products =
                productQueryService.searchProducts(categoryId, subCategoryId, searchKeyword);
        return ResponseEntity.ok(products);
    }
}
