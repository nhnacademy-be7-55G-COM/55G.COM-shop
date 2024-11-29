package shop.s5g.shop.service.coupon.coupon.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnBean(RedisConfig.class)
public class CouponStorageService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CouponTemplateRepository couponTemplateRepository;

    private static final String COUPON_PREFIX = "coupon:";
    private static final String COUPON_COUNT_PREFIX = "couponCount:";
    private static final String ISSUED_USER_PREFIX = "issuedUser:";
    private static final String COUPON_POLICY_DURATION_PREFIX = "couponPolicyDuration:";

    /**
     * 쿠폰 - 남은 갯수 조회 저장소 ( front 에서도 처리할 예정 )
     * @param couponTemplateId
     * @param couponCnt
     */
    public void setCouponCnt(Long couponTemplateId, Integer couponCnt) {

        String key = COUPON_COUNT_PREFIX + couponTemplateId;

        if (Objects.isNull(redisTemplate.hasKey(key))) {
            log.debug("Redis coupon count repository already exists");
            return;
        }

        redisTemplate.opsForValue().set(key, couponCnt);
    }

    /**
     * 쿠폰 남은 수량 가져오기
     * @param couponTemplateId
     * @return Integer
     */
    public Integer getCouponCnt(Long couponTemplateId) {

        String key = COUPON_COUNT_PREFIX + couponTemplateId;

        return (Integer) redisTemplate.opsForValue().get(key);
    }

    /**
     * 쿠폰 수량 한 개씩 제거
     * @param couponTemplateId
     */
    public void decreaseCouponCnt(Long couponTemplateId) {

        String key = COUPON_COUNT_PREFIX + couponTemplateId;

        redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 발급된 쿠폰 - 사용자에게 넣어주기 전 redis 에 넣어놓을 Coupon List
     * @param couponTemplateId
     * @param couponCode
     */
    public void addIssuedCoupon(Long couponTemplateId, String couponCode) {

        String key = COUPON_PREFIX + couponTemplateId;

        redisTemplate.opsForList().rightPush(key, couponCode);
    }

    /**
     * 발급된 쿠폰 - 사용자에게 넘겨주기
     * @param couponTemplateId
     * @return Coupon
     */
    public String popIssuedCoupon(Long couponTemplateId) {

        String key = COUPON_PREFIX + couponTemplateId;
        String value = (String) redisTemplate.opsForList().index(key, 0);
        redisTemplate.opsForList().leftPop(key);

        return value;
    }

    /**
     * 사용자 - 발급받은 쿠폰 리스트 ( 중복으로 가져감을 막기 위한 처리 )
     * @param couponTemplateId
     * @param userId
     */
    public void addUserCoupon(Long couponTemplateId, Long userId) {

        String key = ISSUED_USER_PREFIX + couponTemplateId;

        redisTemplate.opsForSet().add(key, userId);
    }

    /**
     * 사용자가 이미 그 템플릿에 대한 쿠폰을 발급 받았는 지 체크
     * @param couponTemplateId
     * @param userId
     * @return
     */
    public boolean isUserCouponExists(Long couponTemplateId, Long userId) {

        String key = ISSUED_USER_PREFIX + couponTemplateId;

        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, userId));
    }

    /**
     * 해당 템플릿의 기간
     * @param couponTemplateId
     * @return int
     */
    public int getCouponPolicyDuration(Long couponTemplateId) {

        String key = COUPON_POLICY_DURATION_PREFIX + couponTemplateId;

        Integer duration = (Integer) redisTemplate.opsForValue().get(key);

        if (Objects.isNull(duration)) {

            duration = couponTemplateRepository.findCouponPolicyDurationByCouponTemplateId(couponTemplateId);
            redisTemplate.opsForValue().set(key, duration);
        }

        return duration;
    }
}
