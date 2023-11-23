package springboot.shoppingmall.coupon.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springboot.shoppingmall.client.userservice.response.ResponseUserInformation;
import springboot.shoppingmall.client.userservice.UserServiceClient;
import springboot.shoppingmall.coupon.domain.UserCouponDto;
import springboot.shoppingmall.coupon.domain.UserCouponQueryDto;
import springboot.shoppingmall.coupon.domain.UserCouponQueryRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserCouponQueryService {
    private final UserCouponQueryRepository queryRepository;
    private final UserServiceClient userServiceClient;

    public List<UserCouponQueryDto> findUsersReceivedCoupon(Long couponId) {
        // 1
        List<UserCouponDto> userInfoHasCoupon = queryRepository.getUserIdsHasCoupon(couponId);

        // 2
        List<Long> userIds = userInfoHasCoupon.stream()
                .map(UserCouponDto::getUserId)
                .collect(Collectors.toList());

        // 3
        List<ResponseUserInformation> result = userServiceClient.getUsers(userIds);

        // 4. userInfoHasCoupon + result
        Map<Long, ResponseUserInformation> map = result.stream()
                .collect(Collectors.toMap(
                        ResponseUserInformation::getUserId,
                        responseUserInformation -> responseUserInformation
                ));

        List<UserCouponQueryDto> list = userInfoHasCoupon.stream()
                .map(userCouponDto -> {
                    if (map.containsKey(userCouponDto.getUserId())) {
                        ResponseUserInformation responseUserInformation = map.get(userCouponDto.getUserId());
                        return new UserCouponQueryDto(
                                userCouponDto.getUserId(),
                                responseUserInformation.getUserName(),
                                responseUserInformation.getGrade(),
                                userCouponDto.getUsingDate()
                        );
                    }
                    return null;
                }).collect(Collectors.toList());

        return list;
    }


    public List<UsableCouponDto> findUsableCouponList(Long userId, Long partnersId) {
        return queryRepository.findUsableCouponList(userId, partnersId);
    }
}
