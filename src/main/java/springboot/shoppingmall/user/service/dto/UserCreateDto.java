package springboot.shoppingmall.user.service.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import springboot.shoppingmall.user.domain.User;

@Builder
@AllArgsConstructor
@Getter
public class UserCreateDto {
    private String name;
    private String loginId;
    private String password;
    private String confirmPassword;
    private String telNo;
    private LocalDateTime signUpDate;

    public User toEntity() {
        return User.builder()
                .userName(name)
                .loginId(loginId)
                .password(password)
                .telNo(telNo)
                .signUpDate(signUpDate)
                .build();
    }
}
