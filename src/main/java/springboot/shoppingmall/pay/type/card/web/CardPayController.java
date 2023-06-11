package springboot.shoppingmall.pay.type.card.web;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.pay.type.card.service.CardPayService;

@RequiredArgsConstructor
@RestController
public class CardPayController {

    private final CardPayService cardPayService;

    @PostMapping("/pay/CARD/ready")
    public ResponseEntity<CardReadyResponse> cardPayReady(@RequestBody CardReadyRequest param) {
        MultiValueMap<String, String> formData = param.toFormData();
        CardReadyResponse response = (CardReadyResponse) cardPayService.ready(formData);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/pay/CARD/approve")
    public ResponseEntity<CardApproveResponse> cardPayApprove(@RequestBody CardApproveRequest param) {
        MultiValueMap<String, String> formData = param.toFormData();
        CardApproveResponse response = (CardApproveResponse) cardPayService.approve(formData);
        return ResponseEntity.ok().body(response);
    }
}
