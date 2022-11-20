package springboot.shoppingmall.dto.user;

import lombok.Data;
import springboot.shoppingmall.domain.user.User;

@Data
public class FindIdResponse {
    private String loginId;

    public static FindIdResponse of(User user){
        FindIdResponse findIdResponse = new FindIdResponse();
        findIdResponse.loginId = user.getLoginId();
        return findIdResponse;
    }
}
