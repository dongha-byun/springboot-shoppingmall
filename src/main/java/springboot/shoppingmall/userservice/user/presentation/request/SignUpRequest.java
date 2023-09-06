package springboot.shoppingmall.userservice.user.presentation.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.userservice.user.application.dto.SignUpRequestDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private String confirmPassword;
    private String telNo;

    public SignUpRequestDto toDto() {
        return SignUpRequestDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .confirmPassword(confirmPassword)
                .telNo(telNo)
                .signUpDate(LocalDateTime.now())
                .build();
    }
}
