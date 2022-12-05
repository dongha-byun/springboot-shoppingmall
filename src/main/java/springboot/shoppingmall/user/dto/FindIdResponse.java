package springboot.shoppingmall.user.dto;

import lombok.Data;
import springboot.shoppingmall.user.domain.User;

@Data
public class FindIdResponse {
    private String loginId;

    public static FindIdResponse of(User user){
        FindIdResponse findIdResponse = new FindIdResponse();
        findIdResponse.loginId = user.getLoginId();
        return findIdResponse;
    }
}
