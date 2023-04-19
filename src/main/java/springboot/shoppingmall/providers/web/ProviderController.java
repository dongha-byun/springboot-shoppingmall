package springboot.shoppingmall.providers.web;

import static springboot.shoppingmall.providers.web.ProviderRequest.*;
import static springboot.shoppingmall.providers.web.ProviderResponse.*;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.message.MessageProvider;
import springboot.shoppingmall.providers.authentication.AuthorizedPartner;
import springboot.shoppingmall.providers.authentication.LoginPartner;
import springboot.shoppingmall.providers.dto.ProviderDto;
import springboot.shoppingmall.providers.service.ProviderService;
import springboot.shoppingmall.providers.web.exception.NotEqualPasswordException;

@RequiredArgsConstructor
@RestController
public class ProviderController {

    private final ProviderService providerService;
    private final MessageProvider messageProvider;

    @PostMapping("/providers")
    public ResponseEntity<APIResult<ProviderResponse>> createProvider(
            @Validated @RequestBody ProviderRequest providerRequest,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            throw new IllegalArgumentException(bindingResult.getFieldError().getDefaultMessage());
        }

        if(!providerRequest.getPassword().equals(providerRequest.getConfirmPassword())) {
            throw new NotEqualPasswordException(messageProvider.getMessage("provider.validation.notEqualPassword"));
        }

        ProviderDto providerDto = providerService.createProvider(toDto(providerRequest));
        ProviderResponse response = of(providerDto);
        APIResult<ProviderResponse> apiResult = new APIResult<>(messageProvider.getMessage("provider.create.success"), response);

        return ResponseEntity.created(URI.create("/providers/"+response.getId())).body(apiResult);
    }
}
