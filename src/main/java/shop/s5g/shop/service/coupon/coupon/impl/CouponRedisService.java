package shop.s5g.shop.service.coupon.coupon.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import shop.s5g.shop.config.RedisConfig;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnBean(RedisConfig.class)
public class CouponRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 쿠폰 - 남은 갯수 조회 저장소 ( front 에서도 처리할 예정 )
     * @param key
     * @param value
     */
    public void setCouponCnt(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 발급된 쿠폰 - 사용자에게 넣어주기 전 redis 에 넣어놓을 Coupon List
     * @param key
     * @param value
     */
    public void setIssuedCoupon(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 사용자 - 발급받은 쿠폰 리스트 ( 중복으로 가져감을 막기 위한 처리 )
     * @param key
     * @param value
     */
    public void setUserCoupon(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }
}
