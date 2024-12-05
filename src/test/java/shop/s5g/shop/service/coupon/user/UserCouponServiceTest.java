package shop.s5g.shop.service.coupon.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.user.InValidUsedCouponResponseDto;
import shop.s5g.shop.dto.coupon.user.ValidUserCouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.entity.coupon.UserCoupon;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.repository.coupon.user.UserCouponRepository;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.service.coupon.coupon.CouponService;
import shop.s5g.shop.service.coupon.user.impl.UserCouponServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserCouponServiceTest {

    @Mock
    private UserCouponRepository userCouponRepository;
    @Mock
    private CouponService couponService;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private UserCouponServiceImpl userCouponService;

    private CouponTemplate couponTemplate;

    @BeforeEach
    void setUp() {
        CouponPolicy couponPolicy = new CouponPolicy(
            new BigDecimal("0.5"),
            20000L,
            null,
            30
        );

        couponTemplate = new CouponTemplate(
            couponPolicy,
            "Welcome Coupon",
            "This is a Welcome Coupon"
        );

    }

    @Test
    @DisplayName("생일 쿠폰 생성 테스트")
    void createWelcomeCouponTest() {
        // Given
        Member member = new Member();

        Coupon coupon = new Coupon(
            couponTemplate,
            "aAzZxXqQwW"
        );

        when(couponService.createWelcomeCoupon()).thenReturn(coupon);

        // When
        userCouponService.createWelcomeCoupon(member);

        // Then
        verify(userCouponRepository).save(any(UserCoupon.class));
    }

    @Test
    @DisplayName("사용하지 않은 쿠폰 리스트 테스트")
    void getUnusedCouponTest() {
        // Given
        Long customerId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Member member = Member.builder()
                                .id(customerId)
                                .build();
        LocalDateTime now = LocalDateTime.now();

        List<ValidUserCouponResponseDto> couponList = List.of(
            new ValidUserCouponResponseDto(1L, 1L, "aAzZxXcCvV", "Book Coupon", "Is a Book Coupon", 30000L, new BigDecimal("0.5"), 3000L, now, now.plusDays(30)),
            new ValidUserCouponResponseDto(2L, 1L, "aAzZxXcCvG", "Book Coupon", "Is a Book Coupon", 30000L, new BigDecimal("0.5"), 3000L, now, now.plusDays(30))
        );
        Page<ValidUserCouponResponseDto> page = new PageImpl<>(couponList, pageable, couponList.size());

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(userCouponRepository.findUnusedCouponList(customerId, pageable)).thenReturn(page);

        // When
        Page<ValidUserCouponResponseDto> result = userCouponService.getUnusedCoupons(customerId, pageable);

        // Then
        assertEquals(2, result.getTotalElements());
        verify(userCouponRepository).findUnusedCouponList(customerId, pageable);
    }

    @Test
    @DisplayName("사용한 쿠폰 리스트 테스트")
    void getUsedCouponTest() {
        // Given
        Long customerId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Member member = Member.builder()
            .id(customerId)
            .build();

        List<InValidUsedCouponResponseDto> unUsedCouponList = List.of(
            new InValidUsedCouponResponseDto(1L, "aAzZxXcCvV", "Category Coupon", "Is a Category Coupon", 20000L, new BigDecimal("0.5"), null),
            new InValidUsedCouponResponseDto(2L, "aAzZxXcCvE", "Category Coupon", "Is a Category Coupon", 20000L, new BigDecimal("0.5"), null)
        );
        Page<InValidUsedCouponResponseDto> page = new PageImpl<>(unUsedCouponList, pageable, unUsedCouponList.size());

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(userCouponRepository.findUsedCouponList(customerId, pageable)).thenReturn(page);

        // When
        Page<InValidUsedCouponResponseDto> result = userCouponService.getUsedCoupons(customerId, pageable);

        // Then
        assertEquals(2, result.getTotalElements());
        verify(userCouponRepository).findUsedCouponList(customerId, pageable);
    }

    @Test
    @DisplayName("기간이 만료된 쿠폰 리스트 테스트")
    void expiredCouponTest() {
        // Given
        Long customerId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Member member = Member.builder()
            .id(customerId)
            .build();

        List<InValidUsedCouponResponseDto> unUsedCouponList = List.of(
            new InValidUsedCouponResponseDto(1L, "aAzZxXcCvV", "Category Coupon", "Is a Category Coupon", 20000L, new BigDecimal("0.5"), null),
            new InValidUsedCouponResponseDto(2L, "aAzZxXcCvE", "Category Coupon", "Is a Category Coupon", 20000L, new BigDecimal("0.5"), null)
        );
        Page<InValidUsedCouponResponseDto> page = new PageImpl<>(unUsedCouponList, pageable, unUsedCouponList.size());

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(userCouponRepository.findAfterExpiredUserCouponList(customerId, pageable)).thenReturn(page);

        // When
        Page<InValidUsedCouponResponseDto> result = userCouponService.getExpiredCoupons(customerId, pageable);

        // Then
        assertEquals(2, result.getTotalElements());
        verify(userCouponRepository).findAfterExpiredUserCouponList(customerId, pageable);
    }

    @Test
    @DisplayName("사용했거나, 기간이 지난 쿠폰 리스트 테스트")
    void invalidCouponTest() {
        // Given
        Long customerId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Member member = Member.builder()
            .id(customerId)
            .build();

        List<InValidUsedCouponResponseDto> unUsedCouponList = List.of(
            new InValidUsedCouponResponseDto(1L, "aAzZxXcCvV", "Category Coupon", "Is a Category Coupon", 20000L, new BigDecimal("0.5"), null),
            new InValidUsedCouponResponseDto(2L, "aAzZxXcCvE", "Category Coupon", "Is a Category Coupon", 20000L, new BigDecimal("0.5"), null)
        );
        Page<InValidUsedCouponResponseDto> page = new PageImpl<>(unUsedCouponList, pageable, unUsedCouponList.size());

        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(userCouponRepository.findInvalidUserCouponList(customerId, pageable)).thenReturn(page);

        // When
        Page<InValidUsedCouponResponseDto> result = userCouponService.getInValidCoupons(customerId, pageable);

        // Then
        assertEquals(2, result.getTotalElements());
        verify(userCouponRepository).findInvalidUserCouponList(customerId, pageable);
    }
}
