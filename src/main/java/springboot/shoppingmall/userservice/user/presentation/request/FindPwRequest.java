package springboot.shoppingmall.userservice.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPwRequest {

    private String name;
    private String telNo;
    private String email;
}
