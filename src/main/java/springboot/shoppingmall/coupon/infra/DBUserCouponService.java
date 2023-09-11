package springboot.shoppingmall.coupon.infra;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import springboot.shoppingmall.coupon.client.UserCouponService;
import springboot.shoppingmall.userservice.user.domain.User;
import springboot.shoppingmall.userservice.user.domain.UserFinder;
import springboot.shoppingmall.userservice.user.domain.UserGrade;

@RequiredArgsConstructor
@Component
public class DBUserCouponService implements UserCouponService {

    private final UserFinder userFinder;

    @Override
    public List<Long> getUserIdsAboveTheGrade(String targetGrade) {
        List<User> userOverTheUserGrade = userFinder.findUserOverTheUserGrade(UserGrade.valueOf(targetGrade));

        return userOverTheUserGrade.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }
}
