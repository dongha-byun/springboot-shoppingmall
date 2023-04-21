package springboot.shoppingmall.product.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.product.dto.ProductDto;
import springboot.shoppingmall.product.dto.ProductRequest;
import springboot.shoppingmall.product.dto.ProductResponse;
import springboot.shoppingmall.product.service.ProductService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;
    private static final String THUMBNAIL_PATH = "/Users/byundongha/byun/spring/file_dir/shopping_upload/image/";

    @PostMapping(value = "/products")
    public ResponseEntity<ProductResponse> createProduct(@LoginPartner AuthorizedPartner partner,
                                                         @RequestPart(name = "data") ProductRequest productRequest,
                                                         @RequestPart(name = "file") MultipartFile showImgFile) throws IOException {
        String storedThumbnailName = "";
        String viewThumbnailName = "";
        if(!showImgFile.isEmpty()) {
            viewThumbnailName = showImgFile.getOriginalFilename();
            storedThumbnailName = UUID.randomUUID().toString();
            showImgFile.transferTo(new File(THUMBNAIL_PATH + storedThumbnailName));
        }

        ProductDto productDto = ProductRequest.toDto(productRequest, partner.getId(), storedThumbnailName,
                viewThumbnailName);

        ProductResponse productResponse = productService.saveProduct(partner.getId(), productDto);
        return ResponseEntity.created(URI.create("/products/"+productResponse.getId())).body(productResponse);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@AuthenticationStrategy(required = false) AuthorizedUser user,
                                                      @PathVariable("id") Long id){
        ProductResponse productResponse = productService.findProduct(id);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/thumbnail/{fileName}")
    public Resource getThumbnail(@PathVariable("fileName") String fileName) throws MalformedURLException {
        return new UrlResource("file:" + THUMBNAIL_PATH + fileName);
    }
}
