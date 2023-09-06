package springboot.shoppingmall.userservice.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindIdRequest {
    private String name;
    private String telNo;
}
