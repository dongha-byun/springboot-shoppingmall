package springboot.shoppingmall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springboot.shoppingmall.user.domain.User;
import springboot.shoppingmall.user.service.dto.UserDto;
import springboot.shoppingmall.utils.DateUtils;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String telNo;
    private String signUpDate;

    public static UserResponse of(UserDto dto) {
        return UserResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .telNo(dto.getTelNo())
                .signUpDate(DateUtils.toStringOfLocalDateTIme(dto.getSignUpDate(), "yyyy-MM-dd"))
                .build();
    }
}
