package springboot.shoppingmall.user.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.service.dto.FindEmailRequestDto;

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
