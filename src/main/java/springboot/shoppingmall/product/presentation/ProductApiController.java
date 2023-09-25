package springboot.shoppingmall.product.presentation;

import java.io.IOException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.common.validation.bean.BeanValidation;
import springboot.shoppingmall.common.validation.bean.BeanValidationException;
import springboot.shoppingmall.product.application.dto.ProductDto;
import springboot.shoppingmall.product.presentation.request.ProductRequest;
import springboot.shoppingmall.product.presentation.response.ProductResponse;
import springboot.shoppingmall.product.application.dto.ProductCreateDto;
import springboot.shoppingmall.product.application.ProductService;
import springboot.shoppingmall.product.application.ThumbnailInfo;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@Slf4j
@BeanValidation
@RequiredArgsConstructor
@RestController
public class ProductApiController {

    private final ProductService productService;
    private final ThumbnailFileService thumbnailFileService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponse> createProduct(@LoginPartner AuthorizedPartner partner,
                                                         @RequestPart(name = "data") @Validated ProductRequest productRequest,
                                                         BindingResult bindingResult,
                                                         @RequestPart(name = "file") MultipartFile showImgFile) throws IOException {
        if(bindingResult.hasErrors()) {
            throw new BeanValidationException(bindingResult);
        }

        ThumbnailInfo thumbnailInfo = thumbnailFileService.save(showImgFile);

        ProductCreateDto productCreateDto = productRequest.toDto(thumbnailInfo);
        ProductDto productDto = productService.saveProduct(partner.getId(), productCreateDto);
        ProductResponse response = ProductResponse.of(productDto);

        return ResponseEntity.created(URI.create("/products/"+response.getId())).body(response);
    }
}
