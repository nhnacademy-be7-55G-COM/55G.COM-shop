package shop.s5g.shop.controller.delivery;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.shop.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.shop.dto.delivery.DeliveryFeeUpdateRequestDto;
import shop.s5g.shop.service.delivery.DeliveryFeeService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/shop/delivery/fee")
public class DeliveryFeeController {
    private final DeliveryFeeService deliveryFeeService;

    // TODO: 나중에 ADMIN role로 바꿔야 함.
    @PutMapping
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    public ResponseEntity<DeliveryFeeResponseDto> updateDeliveryFee(
        @RequestBody @Valid
        DeliveryFeeUpdateRequestDto updateRequest
    ) {
        return ResponseEntity.ok(deliveryFeeService.updateFee(updateRequest));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryFeeResponseDto>> fetchDeliveryFees() {
        return ResponseEntity.ok(deliveryFeeService.getAllDeliveryFees());
    }

    // TODO: 나중에 ADMIN role로 바꿔야 함.
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping
    public ResponseEntity<DeliveryFeeResponseDto> createDeliveryFee(
        @RequestBody @Valid
        DeliveryFeeCreateRequestDto createRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            deliveryFeeService.createFee(createRequest)
        );
    }
}
