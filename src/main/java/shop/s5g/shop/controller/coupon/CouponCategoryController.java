package shop.s5g.shop.controller.coupon;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.coupon.category.CouponCategoryRequestDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.service.coupon.category.CouponCategoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin/coupons")
public class CouponCategoryController {

    private final CouponCategoryService couponCategoryService;

    @PostMapping("/category")
    public ResponseEntity<MessageDto> createCouponCategory(
        @Valid @RequestBody CouponCategoryRequestDto couponCategoryRequestDto,
        BindingResult result) {

        if (result.hasErrors()) {
            throw new BadRequestException("잘못된 요청입니다.");
        }

        couponCategoryService.createCouponCategory(couponCategoryRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDto("카테고리 쿠폰 생성 성공"));
    }
}
