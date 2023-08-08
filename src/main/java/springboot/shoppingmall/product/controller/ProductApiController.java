package springboot.shoppingmall.product.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.product.dto.ProductDto;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.service.ProductCreateDto;
import springboot.shoppingmall.product.service.ProductService;
import springboot.shoppingmall.product.service.ThumbnailInfo;
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

    @GetMapping("/thumbnail/{fileName}")
    public Resource getThumbnail(@PathVariable("fileName") String fileName) throws MalformedURLException {
        return new UrlResource(thumbnailFileService.getRealFilePath(fileName));
    }
}
