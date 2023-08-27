package springboot.shoppingmall.user.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.service.dto.FindEmailResultDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindEmailResultResponse {
    private String email;

    public static FindEmailResultResponse of(FindEmailResultDto dto) {
        return new FindEmailResultResponse(dto.getEmail());
    }
}
