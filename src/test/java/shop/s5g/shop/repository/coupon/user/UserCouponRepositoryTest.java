package shop.s5g.shop.repository.coupon.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.coupon.user.InValidUsedCouponResponseDto;
import shop.s5g.shop.dto.coupon.user.ValidUserCouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.entity.coupon.CouponTemplate.CouponTemplateType;
import shop.s5g.shop.entity.coupon.UserCoupon;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.member.MemberGrade;
import shop.s5g.shop.entity.member.MemberStatus;
import shop.s5g.shop.repository.coupon.coupon.CouponRepository;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.repository.member.CustomerRepository;
import shop.s5g.shop.repository.member.MemberGradeRepository;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.repository.member.MemberStatusRepository;

@DataJpaTest(includeFilters = {
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CouponRepository.class),
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CouponTemplateRepository.class),
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CouponPolicyRepository.class),
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MemberRepository.class),
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = UserCouponRepository.class),
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CustomerRepository.class),
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MemberStatusRepository.class),
    @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MemberGradeRepository.class),
})
@ActiveProfiles("local")
@Import(TestQueryFactoryConfig.class)
class UserCouponRepositoryTest {

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;
    @Autowired
    private CouponTemplateRepository couponTemplateRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MemberGradeRepository memberGradeRepository;
    @Autowired
    private MemberStatusRepository memberStatusRepository;

    private Member member;
    private final List<Coupon> testCoupons = new ArrayList<>();

    private final Pageable pageable = PageRequest.of(0, 10);
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {

        CouponPolicy couponPolicy = CouponPolicy.builder()
            .condition(30000L)
            .discountPrice(new BigDecimal(1000))
            .maxPrice(null)
            .duration(30)
            .build();

        CouponPolicy savedPolicy = couponPolicyRepository.save(couponPolicy);

        CouponTemplate couponTemplate = new CouponTemplate(
            savedPolicy,
            "Birth Coupon",
            "This is Birth Coupon"
        );

        CouponTemplate savedTemplate = couponTemplateRepository.save(couponTemplate);

        Coupon coupon = new Coupon(
            savedTemplate,
            "zZxXcCdDeE"
        );

        Coupon coupon2 = new Coupon(
            savedTemplate,
            "qQwWeErRtT"
        );

        testCoupons.add(couponRepository.save(coupon));
        testCoupons.add(couponRepository.save(coupon2));

        Customer testCustomer = Customer.builder()
            .email("test@test.com")
            .name("Test User")
            .phoneNumber("010-1234-5678")
            .password("securePassword")
            .active(true)
            .build();

        testCustomer = customerRepository.save(testCustomer);

        MemberGrade memberGrade = new MemberGrade("일반", 0, 3000, true);
        memberGrade = memberGradeRepository.save(memberGrade);

        MemberStatus status = new MemberStatus("일반");
        status = memberStatusRepository.save(status);

        member = Member.builder()
            .customer(testCustomer)
            .grade(memberGrade)
            .status(status)
            .birth("19980808")
            .point(5000L)
            .createdAt(LocalDateTime.now())
            .latestLoginAt(null)
            .password("1q2w3e4r")
            .build();

        member = memberRepository.save(member);
    }

    @Test
    @DisplayName("해당 유저가 똑같은 템플릿을 가지고 있는 지 체크하기")
    void hasCouponTemplate() {

        Coupon addCoupon = testCoupons.removeFirst();

        userCouponRepository.save(new UserCoupon(member, addCoupon, now, now.plusDays(30)));

        boolean result = userCouponRepository.userHasCouponTemplate(member.getId(),
            CouponTemplateType.BIRTH.getTypeName());

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("사용하지 않은 쿠폰 확인하기")
    void findUnusedCoupons() {

        userCouponRepository.save(new UserCoupon(member, testCoupons.removeFirst(), now, now.plusDays(30)));

        Page<ValidUserCouponResponseDto> result = userCouponRepository.findUnusedCouponList(member.getId(), pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getContent().size());
    }

    @Test
    @DisplayName("사용한 쿠폰 확인하기")
    void findUsedCoupons() {

        Coupon usedCoupon = testCoupons.removeFirst();
        usedCoupon.setActive(false);

        UserCoupon userCoupon = new UserCoupon(member, usedCoupon, now, now.plusDays(30));
        userCoupon.setUsedAt(now.plusDays(1));

        userCouponRepository.save(userCoupon);

        Page<InValidUsedCouponResponseDto> usedResult = userCouponRepository.findUsedCouponList(member.getId(), pageable);

        Assertions.assertNotNull(usedResult);
        Assertions.assertEquals(1, usedResult.getContent().size());
    }

    @Test
    @DisplayName("기간이 만료된 쿠폰 확인하기")
    void findExpiredCoupons() {

        Coupon expiredCoupon = testCoupons.removeFirst();
        expiredCoupon.setActive(false);

        userCouponRepository.save(new UserCoupon(member, expiredCoupon, now.minusMinutes(3), now.minusMinutes(2)));

        Page<InValidUsedCouponResponseDto> expiredResult = userCouponRepository.findAfterExpiredUserCouponList(member.getId(), pageable);

        Assertions.assertNotNull(expiredResult);
        Assertions.assertEquals(1, expiredResult.getContent().size());
    }

    @Test
    @DisplayName("기간이 만료되었거나, 사용한 쿠폰 확인")
    void findInvalidCoupons() {

        Coupon invalidCoupon = testCoupons.removeFirst();

        invalidCoupon.setActive(false);

        userCouponRepository.save(new UserCoupon(member, invalidCoupon, now.minusMinutes(3), now.minusMinutes(2)));

        Page<InValidUsedCouponResponseDto> invalidResult = userCouponRepository.findInvalidUserCouponList(member.getId(), pageable);

        Assertions.assertNotNull(invalidResult);
        Assertions.assertEquals(1, invalidResult.getContent().size());
    }

}
