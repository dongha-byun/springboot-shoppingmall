package springboot.shoppingmall.cart.web;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springboot.shoppingmall.authorization.AuthenticationStrategy;
import springboot.shoppingmall.authorization.AuthorizedUser;
import springboot.shoppingmall.cart.service.CartService;

@RequiredArgsConstructor
@RestController
public class CartApiController {
    private final CartService cartService;

    @PostMapping("/carts")
    public ResponseEntity<CartResponse> createCart(@AuthenticationStrategy AuthorizedUser user,
                                                     @RequestBody CartRequest cartRequest){
        CartResponse cartResponse = cartService.create(user.getId(), cartRequest);
        return ResponseEntity.created(URI.create("/carts/"+cartResponse.getId())).body(cartResponse);
    }

    @DeleteMapping("/carts/{id}")
    public ResponseEntity<CartResponse> deleteCart(@AuthenticationStrategy AuthorizedUser user,
                                                     @PathVariable("id") Long id){
        cartService.delete(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartResponse>> findAllCarts(@AuthenticationStrategy AuthorizedUser user){
        List<CartResponse> carts = cartService.findAllByUser(user.getId());
        return ResponseEntity.ok(carts);
    }
}
