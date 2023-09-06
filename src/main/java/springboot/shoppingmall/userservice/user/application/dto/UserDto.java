package springboot.shoppingmall.userservice.user.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.user.domain.User;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String telNo;
    private LocalDateTime signUpDate;

    public static UserDto of(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getUserName())
                .email(user.getEmail())
                .telNo(user.telNo())
                .signUpDate(user.getSignUpDate())
                .build();
    }
}
