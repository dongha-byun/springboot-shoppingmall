package springboot.shoppingmall.providers.presentation;

import static springboot.shoppingmall.providers.presentation.request.ProviderRequest.*;
import static springboot.shoppingmall.providers.presentation.response.ProviderResponse.*;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.message.MessageProvider;
import springboot.shoppingmall.providers.dto.ProviderDto;
import springboot.shoppingmall.providers.application.ProviderService;
import springboot.shoppingmall.providers.presentation.exception.NotEqualPasswordException;
import springboot.shoppingmall.providers.presentation.request.ProviderRequest;
import springboot.shoppingmall.providers.presentation.response.ProviderResponse;

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
