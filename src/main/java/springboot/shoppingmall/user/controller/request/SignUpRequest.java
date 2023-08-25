package springboot.shoppingmall.user.controller.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.service.dto.UserCreateDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String telNo;

    public UserCreateDto toDto() {
        return UserCreateDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .confirmPassword(confirmPassword)
                .telNo(telNo)
                .signUpDate(LocalDateTime.now())
                .build();
    }
}
