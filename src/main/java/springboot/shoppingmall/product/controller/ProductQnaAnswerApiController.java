package springboot.shoppingmall.product.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.product.dto.ProductQnaAnswerRequest;
import springboot.shoppingmall.product.dto.ProductQnaAnswerResponse;
import springboot.shoppingmall.product.service.ProductQnaAnswerService;

@RequiredArgsConstructor
@RestController
public class ProductQnaAnswerApiController {

    private final ProductQnaAnswerService productQnaAnswerService;

    @PostMapping("/products/{productId}/qna/{qnaId}/answer")
    public ResponseEntity<ProductQnaAnswerResponse> createQnaAnswer(@AuthenticationStrategy AuthorizedUser user,
                                                                    @PathVariable("productId") Long productId,
                                                                    @PathVariable("qnaId") Long qnaId,
                                                                    @RequestBody ProductQnaAnswerRequest answerRequest){
        ProductQnaAnswerResponse answerResponse = productQnaAnswerService.createQnaAnswer(qnaId, answerRequest.getContent());

        return ResponseEntity.created(URI.create("/products/"+productId+"/qna/"+qnaId+"/answer/"+answerResponse.getId())).body(answerResponse);

    }
}
