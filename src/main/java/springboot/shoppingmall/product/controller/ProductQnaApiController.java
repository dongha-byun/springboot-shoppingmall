package springboot.shoppingmall.product.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.product.dto.ProductQnaRequest;
import springboot.shoppingmall.product.dto.ProductQnaResponse;
import springboot.shoppingmall.product.service.ProductQnaService;

@RequiredArgsConstructor
@RestController
public class ProductQnaApiController {

    private final ProductQnaService productQnaService;

    @PostMapping("/products/{id}/qna")
    public ResponseEntity<ProductQnaResponse> createQna(@AuthenticationStrategy AuthorizedUser user,
                                                        @PathVariable("id") Long productId,
                                                        @RequestBody ProductQnaRequest request){
        ProductQnaResponse productQnaResponse = productQnaService.createQna(user.getId(), productId, request);
        return ResponseEntity.created(URI.create("/qna/"+productQnaResponse.getId())).body(productQnaResponse);
    }
}
