package springboot.shoppingmall.partners.presentation;

import static springboot.shoppingmall.partners.presentation.request.PartnerRequest.*;
import static springboot.shoppingmall.partners.presentation.response.PartnerResponse.*;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.message.MessageProvider;
import springboot.shoppingmall.partners.dto.PartnerDto;
import springboot.shoppingmall.partners.application.PartnerService;
import springboot.shoppingmall.partners.presentation.exception.NotEqualPasswordException;
import springboot.shoppingmall.partners.presentation.request.PartnerRequest;
import springboot.shoppingmall.partners.presentation.response.PartnerResponse;

@RequiredArgsConstructor
@RestController
public class PartnerController {

    private final PartnerService partnerService;
    private final MessageProvider messageProvider;

    @PostMapping("/partners")
    public ResponseEntity<APIResult<PartnerResponse>> createPartner(
            @Validated @RequestBody PartnerRequest partnerRequest,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getFieldError().getDefaultMessage());
        }

        if(!partnerRequest.getPassword().equals(partnerRequest.getConfirmPassword())) {
            throw new NotEqualPasswordException(messageProvider.getMessage("partners.validation.notEqualPassword"));
        }

        PartnerDto partnerDto = partnerService.createPartner(toDto(partnerRequest));
        PartnerResponse response = of(partnerDto);
        APIResult<PartnerResponse> apiResult = new APIResult<>(messageProvider.getMessage("partners.create.success"), response);

        return ResponseEntity.created(URI.create("/partners/"+response.getId())).body(apiResult);
    }
}
