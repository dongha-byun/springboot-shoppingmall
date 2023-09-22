package springboot.shoppingmall.product.presentation;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.product.presentation.request.ProductQnaAnswerRequest;
import springboot.shoppingmall.product.presentation.response.ProductQnaAnswerResponse;
import springboot.shoppingmall.product.application.ProductQnaAnswerService;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;

@RequiredArgsConstructor
@RestController
public class ProductQnaAnswerApiController {

    private final ProductQnaAnswerService productQnaAnswerService;

    @PostMapping("/products/{productId}/qna/{qnaId}/answer")
    public ResponseEntity<ProductQnaAnswerResponse> createQnaAnswer(
            @LoginPartner AuthorizedPartner partner,
            @PathVariable("productId") Long productId,
            @PathVariable("qnaId") Long qnaId,
            @RequestBody ProductQnaAnswerRequest answerRequest){
        ProductQnaAnswerResponse answerResponse =
                productQnaAnswerService.createQnaAnswer(qnaId, answerRequest.getContent());

        return ResponseEntity
                .created(URI.create("/products/"+productId+"/qna/"+qnaId+"/answer/"+answerResponse.getId()))
                .body(answerResponse);
    }
}
