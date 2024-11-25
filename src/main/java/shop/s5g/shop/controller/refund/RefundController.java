package shop.s5g.shop.controller.refund;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.refund.RefundHistoryCreateRequestDto;
import shop.s5g.shop.dto.refund.RefundHistoryCreateResponseDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.order.RefundHistoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/refund")
public class RefundController {
    private final RefundHistoryService refundHistoryService;

    @PostMapping
    public ResponseEntity<RefundHistoryCreateResponseDto> createRefund(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        @RequestBody @Valid
        RefundHistoryCreateRequestDto refundRequest,
        BindingResult errors
    ) {
        if (errors.hasErrors()) {
            throw new BadRequestException("환불 정보가 잘못되었습니다: "+ errors.toString());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
            refundHistoryService.createNewRefund(memberDetail.getCustomerId(), refundRequest)
        );
    }
}
