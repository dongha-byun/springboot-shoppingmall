package springboot.shoppingmall.user.controller;

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
import springboot.shoppingmall.user.dto.BasketRequest;
import springboot.shoppingmall.user.dto.BasketResponse;
import springboot.shoppingmall.user.service.BasketService;

@RequiredArgsConstructor
@RestController
public class BasketApiController {
    private final BasketService basketService;

    @PostMapping("/baskets")
    public ResponseEntity<BasketResponse> createBasket(@AuthenticationStrategy AuthorizedUser user,
                                                       @RequestBody BasketRequest basketRequest){
        BasketResponse basketResponse = basketService.create(user.getId(), basketRequest);
        return ResponseEntity.created(URI.create("/baskets/"+basketResponse.getId())).body(basketResponse);
    }

    @DeleteMapping("/baskets/{id}")
    public ResponseEntity<BasketResponse> deleteBasket(@AuthenticationStrategy AuthorizedUser user,
                                                       @PathVariable("id") Long id){
        basketService.delete(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/baskets")
    public ResponseEntity<List<BasketResponse>> findAllBaskets(@AuthenticationStrategy AuthorizedUser user){
        List<BasketResponse> baskets = basketService.findAllByUser(user.getId());
        return ResponseEntity.ok(baskets);
    }
}
