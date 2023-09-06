package springboot.shoppingmall.userservice.user.presentation.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.user.application.dto.FindEmailRequestDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindEmailRequest {
    private String name;
    private String telNo;

    public FindEmailRequestDto toDto() {
        return FindEmailRequestDto.builder()
                .name(this.name)
                .telNo(this.telNo)
                .build();
    }
}
