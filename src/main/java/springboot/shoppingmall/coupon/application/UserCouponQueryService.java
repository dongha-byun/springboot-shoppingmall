package springboot.shoppingmall.coupon.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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
    private final CircuitBreakerFactory circuitBreakerFactory;

    public List<UserCouponQueryDto> findUsersReceivedCoupon(Long couponId) {
        // 1
        List<UserCouponDto> userInfoHasCoupon = queryRepository.getUserIdsHasCoupon(couponId);

        // 2
        List<Long> userIds = fetchUserIds(userInfoHasCoupon);

        // 3
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("getUsersReceivedCoupon");
        List<ResponseUserInformation> result = circuitBreaker.run(
                () -> userServiceClient.getUsers(userIds),
                throwable -> new ArrayList<>()
        );

        // 4. userInfoHasCoupon + result
        Map<Long, ResponseUserInformation> map = getResponseUserInformationMap(result);

        return userInfoHasCoupon.stream()
                .map(userCouponDto -> generateUserQueryDto(userCouponDto, map))
                .collect(Collectors.toList());
    }

    private UserCouponQueryDto generateUserQueryDto(UserCouponDto userCouponDto,
                                                     Map<Long, ResponseUserInformation> map) {
        if (map.containsKey(userCouponDto.getUserId())) {
            return convertUserCouponDtoToUserCouponQueryDto(userCouponDto, map);
        }
        return null;
    }

    private List<Long> fetchUserIds(List<UserCouponDto> userInfoHasCoupon) {
        return userInfoHasCoupon.stream()
                .map(UserCouponDto::getUserId)
                .collect(Collectors.toList());
    }

    private Map<Long, ResponseUserInformation> getResponseUserInformationMap(List<ResponseUserInformation> result) {
        return result.stream()
                .collect(Collectors.toMap(
                        ResponseUserInformation::getUserId,
                        responseUserInformation -> responseUserInformation
                ));
    }

    private UserCouponQueryDto convertUserCouponDtoToUserCouponQueryDto(UserCouponDto userCouponDto,
                                                                        Map<Long, ResponseUserInformation> map) {
        ResponseUserInformation responseUserInformation = map.get(userCouponDto.getUserId());
        return new UserCouponQueryDto(
                userCouponDto.getUserId(),
                responseUserInformation.getUserName(),
                responseUserInformation.getGrade(),
                userCouponDto.getUsingDate()
        );
    }


    public List<UsableCouponDto> findUsableCouponList(Long userId, Long partnersId) {
        return queryRepository.findUsableCouponList(userId, partnersId);
    }
}
