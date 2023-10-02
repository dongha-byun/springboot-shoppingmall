package springboot.shoppingmall.product.query.presentation;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.product.query.PagingDataResponse;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;
import springboot.shoppingmall.product.query.dto.ProductQueryResponse;
import springboot.shoppingmall.product.query.application.ProductQueryService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

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
        List<ProductQueryDto> dtos = productQueryService.findProductByOrder(
                categoryId, subCategoryId, ProductQueryOrderType.valueOf(orderType), limit, offset
        );
        List<ProductQueryResponse> products = dtos.stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
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

        List<ProductQueryDto> dtos = productQueryService.findProductByOrder(
                categoryId, subCategoryId, ProductQueryOrderType.valueOf(orderType), limit, offset
        );
        List<ProductQueryResponse> products = dtos.stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PagingDataResponse<>(products));
    }

    @GetMapping("/search-products")
    public ResponseEntity<PagingDataResponse<List<ProductQueryResponse>>> searchProducts(
            @RequestParam("searchKeyword") String searchKeyword,
            @RequestParam("orderType") String orderType,
            @RequestParam("limit") int limit,
            @RequestParam("offset") int offset
    ) {
        List<ProductQueryDto> products = productQueryService.searchProducts(
                searchKeyword, ProductQueryOrderType.valueOf(orderType), limit, offset
        );
        List<ProductQueryResponse> responses = products.stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PagingDataResponse<>(responses));
    }

    @GetMapping("/search-products-more")
    public ResponseEntity<PagingDataResponse<List<ProductQueryResponse>>> searchMoreProducts(
            @RequestParam("searchKeyword") String searchKeyword,
            @RequestParam("orderType") String orderType,
            @RequestParam("limit") int limit,
            @RequestParam("offset") int offset
    ) {
        List<ProductQueryDto> products = productQueryService.searchProducts(
                searchKeyword, ProductQueryOrderType.valueOf(orderType), limit, offset
        );
        List<ProductQueryResponse> responses = products.stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new PagingDataResponse<>(responses));
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
        List<ProductQueryDto> products = productQueryService.findPartnersProductsAll(
                partner.getId(), categoryId, subCategoryId, limit, offset
        );

        List<ProductQueryResponse> responses = products.stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
        int count = productQueryService.countPartnersProducts(partner.getId(), categoryId, subCategoryId);

        return ResponseEntity.ok().body(
                new PagingDataResponse<>(count, category.getName(), subCategory.getName(), responses)
        );
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductQueryResponse> findProduct(@PathVariable("id") Long id) {
        ProductQueryDto dto = productQueryService.findProductOf(id);
        ProductQueryResponse response = ProductQueryResponse.of(dto);

        return ResponseEntity.ok(response);
    }

}
