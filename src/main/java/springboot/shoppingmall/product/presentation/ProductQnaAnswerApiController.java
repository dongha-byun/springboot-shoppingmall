package springboot.shoppingmall.product.presentation;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.common.validation.bean.BeanValidation;
import springboot.shoppingmall.common.validation.bean.BeanValidationException;
import springboot.shoppingmall.product.application.dto.ProductQnaAnswerDto;
import springboot.shoppingmall.product.presentation.request.ProductQnaAnswerRequest;
import springboot.shoppingmall.product.presentation.response.ProductQnaAnswerResponse;
import springboot.shoppingmall.product.application.ProductQnaAnswerService;
import springboot.shoppingmall.partners.authentication.AuthorizedPartner;
import springboot.shoppingmall.partners.authentication.LoginPartner;

@BeanValidation
@RequiredArgsConstructor
@RestController
public class ProductQnaAnswerApiController {

    private final ProductQnaAnswerService productQnaAnswerService;

    @PostMapping("/products/{productId}/qna/{qnaId}/answer")
    public ResponseEntity<ProductQnaAnswerResponse> createQnaAnswer(
            @LoginPartner AuthorizedPartner partner,
            @PathVariable("productId") Long productId,
            @PathVariable("qnaId") Long qnaId,
            @RequestBody @Valid ProductQnaAnswerRequest answerRequest,
            BindingResult bindingResult){

        if(bindingResult.hasErrors()) {
            throw new BeanValidationException(bindingResult);
        }

        ProductQnaAnswerDto dto = productQnaAnswerService.createQnaAnswer(qnaId, answerRequest.getContent());
        ProductQnaAnswerResponse answerResponse = ProductQnaAnswerResponse.of(dto);

        return ResponseEntity
                .created(URI.create("/products/"+productId+"/qna/"+qnaId+"/answer/"+answerResponse.getId()))
                .body(answerResponse);
    }
}
