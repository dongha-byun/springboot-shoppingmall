package springboot.shoppingmall.userservice.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindEmailResultDto {
    private String email;

    public static FindEmailResultDto of(String email) {
        return new FindEmailResultDto(email);
    }
}
