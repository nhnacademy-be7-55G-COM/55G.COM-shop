package shop.S5G.shop.controller.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.customer.CustomerUpdateRequestDto;
import shop.S5G.shop.dto.member.LoginResponseDto;
import shop.S5G.shop.dto.member.MemberDetailResponseDto;
import shop.S5G.shop.dto.member.MemberRegistrationRequestDto;
import shop.S5G.shop.dto.tag.MessageDto;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.security.ShopMemberDetail;
import shop.S5G.shop.service.member.CustomerService;
import shop.S5G.shop.service.member.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class MemberController {

    private final MemberService memberService;
    private final CustomerService customerService;

    @PostMapping("/member")
    public ResponseEntity<MessageDto> registerMember(@Valid @RequestBody
    MemberRegistrationRequestDto memberRegistrationRequestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }
        memberService.saveMember(memberRegistrationRequestDto);
        return ResponseEntity.ok(new MessageDto("생성 성공"));
    }

    @GetMapping("/member/login/{loginId}")
    public ResponseEntity<LoginResponseDto> getMemberLoginInfo(@PathVariable String loginId) {

        LoginResponseDto loginResponseDto = memberService.getLoginDto(loginId);
        return ResponseEntity.ok().body(loginResponseDto);
    }

    @GetMapping("/member")
    public ResponseEntity<MemberDetailResponseDto> getMember(@AuthenticationPrincipal
    ShopMemberDetail memberDetail) {
        MemberDetailResponseDto memberResponseDto = memberService.getMemberDto(
            memberDetail.getLoginId());
        return ResponseEntity.ok().body(memberResponseDto);
    }

    @PutMapping("/member/{loginId}")
    public ResponseEntity<Void> updateLatestLoginAt(@PathVariable String loginId) {
        memberService.updateLatestLoginAt(loginId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/member")
    public ResponseEntity<MessageDto> updateMember(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        @Valid @RequestBody CustomerUpdateRequestDto requestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        customerService.updateCustomer(memberDetail.getCustomerId(), requestDto);

        return ResponseEntity.ok().body(new MessageDto("회원 정보가 변경되었습니다."));
    }
}
