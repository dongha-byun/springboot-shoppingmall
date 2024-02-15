package springboot.shoppingmall.partners.presentation;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.common.validation.bean.BeanValidation;
import springboot.shoppingmall.common.validation.bean.BeanValidationException;
import springboot.shoppingmall.partners.application.PartnerService;
import springboot.shoppingmall.partners.application.request.PartnerRegisterRequestDto;
import springboot.shoppingmall.partners.presentation.request.PartnerRegisterRequest;
import springboot.shoppingmall.partners.presentation.response.PartnerRegisterResponse;

@BeanValidation
@RequiredArgsConstructor
@RestController
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping("/partners/register")
    public ResponseEntity<PartnerRegisterResponse> register(@Validated @RequestBody PartnerRegisterRequest registerRequest,
                                                            BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BeanValidationException(bindingResult);
        }

        PartnerRegisterRequestDto requestDto = registerRequest.toDto();
        Long id = partnerService.register(requestDto);

        return ResponseEntity.created(URI.create("/")).body(
                new PartnerRegisterResponse(id, "판매 자격신청이 완료되었습니다.")
        );
    }
}
