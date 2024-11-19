package shop.s5g.shop.service.coupon.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.coupon.user.UserCouponRequestDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.UserCoupon;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.repository.coupon.user.UserCouponRepository;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.service.coupon.coupon.CouponService;
import shop.s5g.shop.service.coupon.user.UserCouponService;

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

        userCouponRepository.save(new UserCoupon(member, coupon));
    }

    /**
     * 생일 쿠폰 생성
     * @param userCouponRequestDto
     */
    @Override
    public void createBirthCoupon(UserCouponRequestDto userCouponRequestDto) {

        Member member = memberRepository.findById(userCouponRequestDto.customerId())
            .orElseThrow(() -> new MemberNotFoundException("해당 멤버를 찾을 수 없습니다."));

        Coupon coupon = couponService.createBirthCoupon();

        userCouponRepository.save(new UserCoupon(member, coupon));
    }
}
