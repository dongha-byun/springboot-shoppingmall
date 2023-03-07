package springboot.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import springboot.shoppingmall.user.domain.User;

@AllArgsConstructor
@Data
public class FindIdResponse {
    private String loginId;
}
