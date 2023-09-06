package springboot.shoppingmall.userservice.user.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FindEmailRequestDto {
    private String name;
    private String telNo;
}
