package springboot.shoppingmall.product.presentation;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.product.application.dto.ProductQnaCreateDto;
import springboot.shoppingmall.product.application.dto.ProductQnaDto;
import springboot.shoppingmall.product.presentation.request.ProductQnaRequest;
import springboot.shoppingmall.product.presentation.response.ProductQnaResponse;
import springboot.shoppingmall.product.application.ProductQnaService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductQnaApiController {

    private final ProductQnaService productQnaService;

    @PostMapping("/products/{id}/qna")
    public ResponseEntity<ProductQnaResponse> createQna(@AuthenticationStrategy AuthorizedUser user,
                                                        @PathVariable("id") Long productId,
                                                        @RequestBody ProductQnaRequest productQnaRequest){
        ProductQnaCreateDto createDto = productQnaRequest.toDto();
        ProductQnaDto dto = productQnaService.createQna(user.getId(), productId, createDto);
        ProductQnaResponse productQnaResponse = ProductQnaResponse.of(dto);

        return ResponseEntity.created(URI.create("/qna/"+productQnaResponse.getId())).body(productQnaResponse);
    }

    @GetMapping("/products/{id}/qna")
    public ResponseEntity<List<ProductQnaResponse>> findAllQna(@PathVariable("id") Long productId){
        List<ProductQnaResponse> allQna = productQnaService.findQnaAllByProduct(productId);
        return ResponseEntity.ok(allQna);
    }

    @GetMapping("/products/{productId}/qna/{qnaId}")
    public ResponseEntity<ProductQnaResponse> findQna(@PathVariable("productId") Long productId,
                                                      @PathVariable("qnaId") Long qnaId){
        ProductQnaResponse qnaResponse = productQnaService.findQnaByProduct(productId, qnaId);
        return ResponseEntity.ok().body(qnaResponse);
    }
}
