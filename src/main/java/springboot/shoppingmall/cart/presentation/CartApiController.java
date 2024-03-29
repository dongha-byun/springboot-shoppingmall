package springboot.shoppingmall.cart.presentation;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.GatewayAuthInfo;
import springboot.shoppingmall.authorization.GatewayAuthentication;
import springboot.shoppingmall.cart.application.CartQueryService;
import springboot.shoppingmall.cart.application.dto.CartDto;
import springboot.shoppingmall.cart.application.CartService;
import springboot.shoppingmall.cart.application.dto.CartQueryDto;
import springboot.shoppingmall.cart.presentation.request.CartRequest;
import springboot.shoppingmall.cart.presentation.response.CartQueryResponse;
import springboot.shoppingmall.cart.presentation.response.CartResponse;

@RequiredArgsConstructor
@RestController
public class CartApiController {
    private final CartService cartService;
    private final CartQueryService cartQueryService;

    @PostMapping("/carts")
    public ResponseEntity<CartResponse> createCart(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                   @RequestBody CartRequest cartRequest){
        CartDto cartDto = cartService.create(gatewayAuthInfo.getUserId(), cartRequest.toDto());
        CartResponse response = CartResponse.of(cartDto);
        return ResponseEntity.created(URI.create("/carts/"+response.getId())).body(response);
    }

    @DeleteMapping("/carts/{id}")
    public ResponseEntity<CartResponse> deleteCart(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo,
                                                   @PathVariable("id") Long id){
        cartService.delete(gatewayAuthInfo.getUserId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartQueryResponse>> findAllCarts(@GatewayAuthentication GatewayAuthInfo gatewayAuthInfo){
        List<CartQueryDto> carts = cartQueryService.findAllByUser(gatewayAuthInfo.getUserId());
        List<CartQueryResponse> cartQueryResponses = carts.stream()
                .map(CartQueryResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(cartQueryResponses);
    }
}
