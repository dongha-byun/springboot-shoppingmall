package springboot.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String name;
    private String telNo;
    private String loginId;

    public static UserResponse of(User user){
        return new UserResponse(user.getUserName(), user.getTelNo() ,user.getLoginId());
    }
}
