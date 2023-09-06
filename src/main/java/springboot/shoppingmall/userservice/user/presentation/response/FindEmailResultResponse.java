package springboot.shoppingmall.userservice.user.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.user.application.dto.FindEmailResultDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindEmailResultResponse {
    private String email;

    public static FindEmailResultResponse of(FindEmailResultDto dto) {
        return new FindEmailResultResponse(dto.getEmail());
    }
}
