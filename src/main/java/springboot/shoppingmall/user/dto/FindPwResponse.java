package springboot.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPwResponse {

    private Long userId;
    private String name;
    private String telNo;
    private String email;

    public static FindPwResponse of(User user){
        return new FindPwResponse(user.getId(), user.getUserName(), user.telNo(), user.getEmail());
    }
}
