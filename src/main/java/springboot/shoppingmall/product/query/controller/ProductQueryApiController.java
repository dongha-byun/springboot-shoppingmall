package springboot.shoppingmall.product.query.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.product.query.PagingDataResponse;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryResponse;
import springboot.shoppingmall.product.query.service.ProductQueryService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductQueryApiController {

    private final ProductQueryService productQueryService;
    private final CategoryFinder categoryFinder;


    @GetMapping("/products")
    public ResponseEntity<PagingDataResponse<List<ProductQueryResponse>>> queryProductsBySort(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("subCategoryId") Long subCategoryId,
            @RequestParam(name = "orderType", defaultValue = "RECENT") String orderType,
            @RequestParam(name = "limit", defaultValue = "10", required = false) int limit,
            @RequestParam(name = "offset", defaultValue = "0", required = false) int offset){

        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        List<ProductQueryResponse> products = productQueryService.findProductByOrder(categoryId,
                subCategoryId, ProductQueryOrderType.valueOf(orderType), limit, offset);
        int totalCount = productQueryService.getTotalCount(categoryId, subCategoryId);

        return ResponseEntity.ok(new PagingDataResponse<>(totalCount, category.getName(), subCategory.getName(), products));
    }

    @GetMapping("/products-more")
    public ResponseEntity<PagingDataResponse<List<ProductQueryResponse>>> queryProductMore(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("subCategoryId") Long subCategoryId,
            @RequestParam(name = "orderType", defaultValue = "RECENT") String orderType,
            @RequestParam(name = "limit", defaultValue = "10", required = false) int limit,
            @RequestParam(name = "offset", defaultValue = "0", required = false) int offset){

        List<ProductQueryResponse> products = productQueryService.findProductByOrder(categoryId,
                subCategoryId, ProductQueryOrderType.valueOf(orderType), limit, offset);

        return ResponseEntity.ok(new PagingDataResponse<>(products));
    }

    @GetMapping("/search-products")
    public ResponseEntity<List<ProductQueryResponse>> searchProducts(@RequestParam("categoryId") Long categoryId,
                                                                     @RequestParam("subCategoryId") Long subCategoryId,
                                                                     @RequestParam("searchKeyword") String searchKeyword) {
        List<ProductQueryResponse> products =
                productQueryService.searchProducts(categoryId, subCategoryId, searchKeyword);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/partners/products")
    public ResponseEntity<PagingDataResponse<List<ProductQueryResponse>>> findAllPartnersProducts(
            @LoginPartner AuthorizedPartner partner,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("subCategoryId") Long subCategoryId,
            @RequestParam(name = "limit", defaultValue = "10", required = false) int limit,
            @RequestParam(name = "offset", defaultValue = "0", required = false) int offset) {

        Category category = categoryFinder.findById(categoryId);
        Category subCategory = categoryFinder.findById(subCategoryId);
        List<ProductQueryResponse> productQueryResponses = productQueryService.findPartnersProductsAll(partner.getId(),
                categoryId, subCategoryId, limit, offset);
        int count = productQueryService.countPartnersProducts(partner.getId(), categoryId, subCategoryId);

        return ResponseEntity.ok().body(
                new PagingDataResponse<>(count, category.getName(), subCategory.getName(),productQueryResponses)
        );
    }
}
