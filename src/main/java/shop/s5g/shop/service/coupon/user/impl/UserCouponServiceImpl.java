package shop.s5g.shop.service.coupon.user.impl;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.dto.coupon.user.InValidUsedCouponResponseDto;
import shop.s5g.shop.dto.coupon.user.ValidUserCouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.UserCoupon;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.exception.coupon.CouponAlreadyIssuedException;
import shop.s5g.shop.exception.coupon.CouponNotFoundException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.repository.coupon.user.UserCouponRepository;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.service.coupon.coupon.CouponService;
import shop.s5g.shop.service.coupon.coupon.impl.CouponStorageService;
import shop.s5g.shop.service.coupon.user.UserCouponService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@ConditionalOnBean(RedisConfig.class)
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponService couponService;
    private final MemberRepository memberRepository;
    private final CouponStorageService couponStorageService;

    /**
     * 유저에게 해당 쿠폰 넣어주기
     * @param member
     */
    @Override
    public void createWelcomeCoupon(Member member) {

        Coupon coupon = couponService.createWelcomeCoupon();

        LocalDateTime now = LocalDateTime.now();

        try {
            if (Objects.nonNull(coupon)) {
                userCouponRepository.save(new UserCoupon(
                    member,
                    coupon,
                    now,
                    now.plusDays(30)));
            } else {
                log.warn("해당 쿠폰이 유효하지 않습니다.");
            }
        } catch (Exception e) {
            log.warn("회원가입 쿠폰 발급에 실패했습니다. 멤버 ID : {}", member.getId());
        }
    }

    /**
     * 유저의 사용하지 않은 쿠폰 가져오기
     * @param customerId
     * @param pageable
     * @return Page<UserCouponResponseDto>
     */
    @Override
    public Page<ValidUserCouponResponseDto> getUnusedCoupons(Long customerId, Pageable pageable) {

        Member member = getMemberById(customerId);

        return userCouponRepository.findUnusedCouponList(member.getId(), pageable);
    }

    /**
     * 사용한 쿠폰 조회
     * @param customerId
     * @param pageable
     * @return Page<InValidUsedCouponResponseDto>
     */
    @Override
    public Page<InValidUsedCouponResponseDto> getUsedCoupons(Long customerId, Pageable pageable) {

        Member member = getMemberById(customerId);

        return userCouponRepository.findUsedCouponList(member.getId(), pageable);
    }

    /**
     * 만료된 쿠폰 조회
     * @param customerId
     * @param pageable
     * @return Page<InValidUsedCouponResponseDto>
     */
    @Override
    public Page<InValidUsedCouponResponseDto> getExpiredCoupons(Long customerId,
        Pageable pageable) {

        Member member = getMemberById(customerId);

        return userCouponRepository.findAfterExpiredUserCouponList(member.getId(), pageable);
    }

    /**
     * 기간이 지났거나 사용한 쿠폰 조회
     * @param customerId
     * @param pageable
     * @return Page<InValidUsedCouponResponseDto>
     */
    @Override
    public Page<InValidUsedCouponResponseDto> getInValidCoupons(Long customerId,
        Pageable pageable) {

        Member member = getMemberById(customerId);

        return userCouponRepository.findInvalidUserCouponList(member.getId(), pageable);
    }

    /**
     * 유저에게 쿠폰 넣어주기
     * @param customerId
     * @param couponTemplateId
     */
    @Override
    public void addUserCoupon(Long customerId, Long couponTemplateId) {

        try {
            // 캐싱된 정책 duration 가져오기
            LocalDateTime now = LocalDateTime.now();
            int templateDuration = couponStorageService.getCouponPolicyDuration(couponTemplateId);

            // 쿠폰 갯수가 있는 지 체크 ( front 단에서도 더블 체크를 할 예정임 )
            if (couponStorageService.getCouponCnt(couponTemplateId) <= 0) {
                throw new CouponNotFoundException("쿠폰 발급이 마감되었습니다.");
            }

            // 사용자가 이미 쿠폰이 있는 지 체크
            if (couponStorageService.isUserCouponExists(couponTemplateId, customerId)) {
                throw new CouponAlreadyIssuedException("이미 쿠폰 발급 받은 유저입니다.");
            }

            // 사용자가 쿠폰이 없다면 user 에게 쿠폰 넣어주기
            Member member = getMemberById(customerId);
            String couponCode = couponStorageService.popIssuedCoupon(couponTemplateId);

            Coupon coupon = couponService.getCouponByCode(couponCode);

            userCouponRepository.save(new UserCoupon(member, coupon, now, now.plusDays(templateDuration)));

            // 해당 쿠폰 수량 하나 지워주기
            couponStorageService.decreaseCouponCnt(couponTemplateId);

            // 쿠폰을 넣어줬다면 redis 발급받은 유저 목록에 넣어주기
            couponStorageService.addUserCoupon(couponTemplateId, customerId);

        } catch (Exception e) {
            log.warn("An error occurred in the process of inserting a coupon to the user ID : {}", customerId);
            throw e;
        }

    }

    /**
     * 멤버 조회
     * @param memberId
     * @return Member
     */
    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없습니다."));
    }
}
