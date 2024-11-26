package shop.s5g.shop.service.coupon.coupon.impl;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.coupon.coupon.AvailableCouponResponseDto;
import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.entity.coupon.CouponTemplate.CouponTemplateType;
import shop.s5g.shop.exception.coupon.CouponAlreadyDeletedException;
import shop.s5g.shop.exception.coupon.CouponNotFoundException;
import shop.s5g.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.shop.repository.coupon.coupon.CouponRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.coupon.CouponService;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponTemplateRepository couponTemplateRepository;
    private final CouponRedisService couponRedisService;

    /**
     * 쿠폰 생성 ( 1 ~ n개 생성 가능 )
     * @param couponCnt
     * @param couponRequestDto
     */
    @Override
    public void createCoupon(Integer couponCnt, CouponRequestDto couponRequestDto) {

        if (Objects.isNull(couponCnt) || couponCnt <= 0) {
            throw new IllegalArgumentException("쿠폰 수량이 잘못 지정되었습니다.");
        }

        CouponTemplate couponTemplate = couponTemplateRepository.findById(couponRequestDto.couponTemplateId())
            .orElseThrow(() -> new CouponTemplateNotFoundException("쿠폰 템플릿을 찾을 수 없습니다."));

        Set<String> uniqueCode = new HashSet<>();
        Set<Coupon> couponSet = new HashSet<>();

        while (uniqueCode.size() < couponCnt) {
            String couponCode = createUniqueCouponNumber();

            if (uniqueCode.contains(couponCode)) {
                continue;
            }
            uniqueCode.add(couponCode);
        }

        int saveCouponCnt = 0;
        for (String code : uniqueCode) {

            Coupon coupon = new Coupon(
                couponTemplate,
                code,
                null,
                null
            );
            // 한 번에 쿠폰 천장까지만 저장할 예정
            if (saveCouponCnt < 1000) {
                couponRedisService.addIssuedCoupon(couponTemplate.getCouponTemplateId(), code);
            }
            couponSet.add(coupon);

            saveCouponCnt++;
        }

        couponRedisService.setCouponCnt(couponTemplate.getCouponTemplateId(), couponCnt);

        couponRepository.saveAll(couponSet);

    }

    /**
     * 웰컴 쿠폰 생성
     * @return Coupon
     */
    @Override
    public Coupon createWelcomeCoupon() {

        CouponTemplate welcomeTemplate = couponTemplateRepository.findParticularCouponByName(
            CouponTemplateType.WELCOME.getTypeName());

        if (Objects.isNull(welcomeTemplate)) {
            return null;
        }

        return couponRepository.save(
            new Coupon(
                welcomeTemplate,
                createUniqueCouponNumber(),
                LocalDateTime.now().plusDays(30)
            )
        );
    }

    /**
     * 생일 쿠폰 생성
     * @return Coupon
     */
    @Override
    public Coupon createBirthCoupon() {

        CouponTemplate birthTemplate = couponTemplateRepository.findParticularCouponByName(
            CouponTemplateType.BIRTH.getTypeName()
        );

        if (Objects.isNull(birthTemplate)) {
            throw new CouponTemplateNotFoundException("해당 쿠폰 템플릿이 존재하지 않습니다.");
        }

        // 쿠폰의 만료일을 해당 월의 마지막 일로 설정
        LocalDateTime now = LocalDateTime.now();
        YearMonth yearMonth = YearMonth.from(now);
        LocalDateTime lastDayOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return couponRepository.save(
            new Coupon(
                birthTemplate,
                createUniqueCouponNumber(),
                lastDayOfMonth
            )
        );
    }

    /**
     * 카테고리와 북 쿠폰 생성
     * @return Coupon - 생성된 쿠폰
     */
    @Override
    public Coupon createCategoryCoupon() {

        CouponTemplate categoryTemplate = couponTemplateRepository.findParticularCouponByName(
            CouponTemplateType.CATEGORY.getTypeName()
        );

        if (Objects.isNull(categoryTemplate)) {
            throw new CouponTemplateNotFoundException("해당 쿠폰 템플릿이 존재하지 않습니다.");
        }

        return couponRepository.save(
            new Coupon(
                categoryTemplate,
                createUniqueCouponNumber(),
                null
            )
        );
    }

    /**
     * 쿠폰 조회
     * @param couponCode
     * @return Coupon
     */
    @Override
    public Coupon getCouponByCode(String couponCode) {

        return couponRepository.findByCouponCode(couponCode);
    }

    /**
     * 특정 쿠폰 조회
     * @param couponId
     * @return CouponResponseDto
     */
    @Override
    public CouponResponseDto getCoupon(Long couponId) {

        if (Objects.isNull(couponId) || couponId < 0) {
            throw new IllegalArgumentException("쿠폰 아이디가 잘못 지정되었습니다.");
        }

        CouponResponseDto couponResponseDto = couponRepository.findCoupon(couponId);

        if (Objects.isNull(couponResponseDto)) {
            throw new CouponNotFoundException("선택하신 쿠폰을 찾으실 수 없습니다.");
        }

        return couponResponseDto;
    }

    /**
     * 발급한 모든 쿠폰 조회
     * @param pageable
     * @return Page<CouponResponseDto> - paging 처리
     */
    @Override
    public Page<CouponResponseDto> getAllCouponList(Pageable pageable) {
        return couponRepository.getAllIssuedCoupons(pageable);
    }

    /**
     * 쿠폰 수정
     * @param couponId
     */
    @Override
    public void updateCoupon(Long couponId, LocalDateTime expiredAt) {

        if (Objects.isNull(couponId) || couponId < 0) {
            throw new IllegalArgumentException("쿠폰 아이디가 잘못 지정되었습니다.");
        }

        if (!couponRepository.existsById(couponId)) {
            throw new CouponNotFoundException("해당 쿠폰이 존재하지 않습니다.");
        }

        if (!couponRepository.checkActiveCoupon(couponId)) {
            throw new CouponAlreadyDeletedException("해당 쿠폰은 삭제된 쿠폰입니다.");
        }

        couponRepository.updateCouponExpiredDatetime(couponId, expiredAt);
    }

    /**
     * 쿠폰 삭제
     * @param couponId
     */
    @Override
    public void deleteCoupon(Long couponId) {
        if (Objects.isNull(couponId) || couponId <= 0) {
            throw new IllegalArgumentException("쿠폰 아이디의 값이 잘못 지정되었습니다.");
        }

        if (!couponRepository.existsById(couponId)) {
            throw new CouponNotFoundException("해당 쿠폰은 존재하지 않는 쿠폰입니다.");
        }

        if (!couponRepository.checkActiveCoupon(couponId)) {
            throw new CouponAlreadyDeletedException("해당 쿠폰은 삭제된 쿠폰입니다.");
        }

        couponRepository.deleteCouponById(couponId);
    }

    /**
     * 발급 가능한 쿠폰 조회
     * @param pageable
     * @return Page<AvailableCouponResponseDto>
     */
    @Override
    public Page<AvailableCouponResponseDto> getAvailableCoupons(Pageable pageable) {

        return couponRepository.getAllAvailableCoupons(pageable);
    }

    /**
     * 쿠폰 번호 랜덤 생성
     * @return String
     */
    private String createUniqueCouponNumber() {

        String uuid = UUID.randomUUID().toString();

        String base62Encoded = encodeBase62(uuid.getBytes());

        return base62Encoded.substring(0, 15);

    }

    private String encodeBase62(byte[] input) {

        String BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder result = new StringBuilder();

        for (byte b : input) {
            result.append(BASE62.charAt(((b & 0xFF) % BASE62.length())));
        }

        return result.toString();
    }
}
