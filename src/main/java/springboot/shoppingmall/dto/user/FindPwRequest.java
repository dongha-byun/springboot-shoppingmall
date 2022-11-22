package springboot.shoppingmall.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPwRequest {

    private String name;
    private String telNo;
    private String loginId;
}
