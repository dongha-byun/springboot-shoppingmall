package springboot.shoppingmall.product.presentation;

import java.io.IOException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.product.application.dto.ProductDto;
import springboot.shoppingmall.product.presentation.request.ProductRequest;
import springboot.shoppingmall.product.presentation.response.ProductResponse;
import springboot.shoppingmall.product.application.dto.ProductCreateDto;
import springboot.shoppingmall.product.application.ProductService;
import springboot.shoppingmall.product.application.ThumbnailInfo;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductApiController {

    private final ProductService productService;
    private final ThumbnailFileService thumbnailFileService;

    @PostMapping(value = "/products")
    public ResponseEntity<ProductResponse> createProduct(@LoginPartner AuthorizedPartner partner,
                                                         @RequestPart(name = "data") ProductRequest productRequest,
                                                         @RequestPart(name = "file") MultipartFile showImgFile) throws IOException {
        ThumbnailInfo thumbnailInfo = thumbnailFileService.save(showImgFile);

        ProductCreateDto productCreateDto = productRequest.toDto(thumbnailInfo);
        ProductDto productDto = productService.saveProduct(partner.getId(), productCreateDto);
        ProductResponse response = ProductResponse.of(productDto);

        return ResponseEntity.created(URI.create("/products/"+response.getId())).body(response);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@AuthenticationStrategy(required = false) AuthorizedUser user,
                                                      @PathVariable("id") Long id){
        ProductResponse productResponse = productService.findProduct(id);
        return ResponseEntity.ok(productResponse);
    }
}
