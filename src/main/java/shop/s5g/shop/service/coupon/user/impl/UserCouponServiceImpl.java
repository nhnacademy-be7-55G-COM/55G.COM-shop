package shop.s5g.shop.service.coupon.user.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.coupon.user.UserCouponRequestDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.UserCoupon;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.exception.coupon.CouponCategoryAlreadyExistsException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.repository.coupon.user.UserCouponRepository;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.service.coupon.coupon.CouponService;
import shop.s5g.shop.service.coupon.user.UserCouponService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponService couponService;
    private final MemberRepository memberRepository;

    /**
     * 유저에게 해당 쿠폰 넣어주기
     * @param userCouponRequestDto
     */
    @Override
    public void createWelcomeCoupon(UserCouponRequestDto userCouponRequestDto) {

        Member member = memberRepository.findById(userCouponRequestDto.customerId())
            .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없습니다."));

        Coupon coupon = couponService.createWelcomeCoupon();

        if (Objects.nonNull(coupon)) {
            userCouponRepository.save(new UserCoupon(member, coupon));
        } else {
            log.warn("회원가입 쿠폰 발급에 실패했습니다. 멤버 ID : {}", member.getId());
        }
    }

    /**
     * 멤버에게 카테고리 쿠폰 넣어주기
     * @param memberId
     * @param categoryName
     */
    @Override
    public void createCategoryCoupon(Long memberId, String categoryName) {

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없습니다."));

        if (userCouponRepository.userHasCouponTemplate(memberId, categoryName)) {
            throw new CouponCategoryAlreadyExistsException("해당 쿠폰을 이미 발급 받으셨습니다.");
        }

        Coupon coupon = couponService.createCategoryCoupon();

        if (Objects.nonNull(coupon)) {
            userCouponRepository.save(new UserCoupon(member, coupon));
        } else {
            log.warn("카테고리 쿠폰 발급에 실패했습니다. 멤버 ID : {}", member.getId());
        }
    }


}
