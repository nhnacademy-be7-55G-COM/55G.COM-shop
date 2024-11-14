package shop.s5g.shop.controller.member;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.address.AddressRequestDto;
import shop.s5g.shop.dto.address.AddressResponseDto;
import shop.s5g.shop.dto.address.AddressUpdateRequestDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.member.AddressService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/address")
    public ResponseEntity<List<AddressResponseDto>> getAddresses(@AuthenticationPrincipal
    ShopMemberDetail detail) {
        List<AddressResponseDto> responseDtos = addressService.getAddresses(detail.getCustomerId());

        return ResponseEntity.ok().body(responseDtos);
    }

    @PostMapping("/address")
    public ResponseEntity<MessageDto> createAddress(
        @Valid @RequestBody AddressRequestDto requestDto,
        BindingResult bindingResult,
        @AuthenticationPrincipal ShopMemberDetail detail) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }
        addressService.save(requestDto, detail.getCustomerId());
        return ResponseEntity.ok().body(new MessageDto("주소 저장 성공"));
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<MessageDto> deleteAddress(@PathVariable Long addressId) {
        addressService.delete(addressId);
        return ResponseEntity.ok().body(new MessageDto("삭제 성공"));
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<MessageDto> updateAddress(@RequestBody AddressUpdateRequestDto requestDto,
        @PathVariable Long addressId) {
        addressService.update(requestDto, addressId);
        return ResponseEntity.ok().body(new MessageDto("변경 성공"));
    }
}
