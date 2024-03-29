package springboot.shoppingmall.product.query.presentation;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.category.domain.Category;
import springboot.shoppingmall.category.domain.CategoryFinder;
import springboot.shoppingmall.partners.authentication.AuthorizedPartner;
import springboot.shoppingmall.partners.authentication.LoginPartner;
import springboot.shoppingmall.product.query.PagingDataResponse;
import springboot.shoppingmall.product.query.ProductQueryOrderType;
import springboot.shoppingmall.product.query.application.ProductQueryService;
import springboot.shoppingmall.product.query.dto.ProductQueryDto;
import springboot.shoppingmall.product.query.dto.ProductQueryResponse;
import springboot.shoppingmall.product.query.presentation.request.ProductQueryRequestParam;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductQueryApiController {

    private final ProductQueryService productQueryService;
    private final CategoryFinder categoryFinder;

    @GetMapping("/products")
    public ResponseEntity<PagingDataResponse<List<ProductQueryResponse>>> queryProductsBySort(ProductQueryRequestParam param) {
        Category category = categoryFinder.findById(param.getCategoryId());
        Category subCategory = categoryFinder.findById(param.getSubCategoryId());
        List<ProductQueryDto> dtos = productQueryService.findProductByOrder(
                param.getCategoryId(), param.getSubCategoryId(),
                param.getOrderType(), param.getLimit(), param.getOffset()
        );
        List<ProductQueryResponse> products = convertDtoToResponse(dtos);
        int totalCount = productQueryService.getTotalCount(param.getCategoryId(), param.getSubCategoryId());

        return ResponseEntity.ok(
                new PagingDataResponse<>(totalCount, category.getName(), subCategory.getName(), products));
    }

    @GetMapping("/products-more")
    public ResponseEntity<PagingDataResponse<List<ProductQueryResponse>>> queryProductMore(ProductQueryRequestParam param){

        List<ProductQueryDto> dtos = productQueryService.findProductByOrder(
                param.getCategoryId(), param.getSubCategoryId(),
                param.getOrderType(), param.getLimit(), param.getOffset()
        );
        List<ProductQueryResponse> products = convertDtoToResponse(dtos);

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
        List<ProductQueryResponse> responses = convertDtoToResponse(products);
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
        List<ProductQueryResponse> responses = convertDtoToResponse(products);
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

        List<ProductQueryResponse> responses = convertDtoToResponse(products);
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

    private List<ProductQueryResponse> convertDtoToResponse(List<ProductQueryDto> dtos) {
        return dtos.stream()
                .map(ProductQueryResponse::of)
                .collect(Collectors.toList());
    }

}
