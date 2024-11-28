package shop.s5g.shop.controller.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.dto.customer.CustomerUpdateRequestDto;
import shop.s5g.shop.dto.member.IdCheckResponseDto;
import shop.s5g.shop.dto.member.LoginResponseDto;
import shop.s5g.shop.dto.member.MemberDetailResponseDto;
import shop.s5g.shop.dto.member.MemberLoginIdResponseDto;
import shop.s5g.shop.dto.member.MemberRegistrationRequestDto;
import shop.s5g.shop.dto.member.PasswordChangeRequestDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.exception.payco.AlreadyLinkAccountException;
import shop.s5g.shop.exception.payco.PaycoNotLinkedException;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.member.CustomerService;
import shop.s5g.shop.service.member.MemberService;

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

    @DeleteMapping("/member")
    public ResponseEntity<MessageDto> deleteMember(
        @AuthenticationPrincipal ShopMemberDetail memberDetail) {
        memberService.deleteById(memberDetail.getCustomerId());
        customerService.deleteCustomer(memberDetail.getCustomerId());
        return ResponseEntity.ok().body(new MessageDto("탈퇴 처리가 완료됐습니다."));
    }

    @PostMapping("/checkId/{loginId}")
    public ResponseEntity<IdCheckResponseDto> checkId(@PathVariable String loginId) {
        IdCheckResponseDto responseDto = memberService.isExistsByLoginId(loginId);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/member/password")
    public ResponseEntity<MessageDto> changePassword(
        @RequestBody PasswordChangeRequestDto requestDto,
        @AuthenticationPrincipal ShopMemberDetail memberDetail) {
        memberService.changePassword(memberDetail.getCustomerId(), requestDto.oldPassword(),
            requestDto.newPassword());

        return ResponseEntity.ok().body(new MessageDto("비밀번호 변경 성공"));
    }

    @GetMapping("/member/payco")
    public ResponseEntity<MemberLoginIdResponseDto> getLoginIdByPaycoId(
        @RequestParam(name = "payco_id") String paycoId) {
        return ResponseEntity.ok().body(memberService.getMemberLoginIdDtoByPaycoId(paycoId));
    }

    @PostMapping("/member/payco/link")
    public ResponseEntity<MessageDto> linkPayco(@RequestParam(name = "payco_id") String paycoId,
        @AuthenticationPrincipal ShopMemberDetail memberDetail) {
        memberService.linkAccountByPaycoId(memberDetail.getCustomerId(), paycoId);
        return ResponseEntity.ok().body(new MessageDto("연동 성공"));
    }

    @ExceptionHandler(PaycoNotLinkedException.class)
    public ResponseEntity<MessageDto> handlePaycoNotLinkedException(PaycoNotLinkedException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageDto(e.getMessage()));
    }

    @ExceptionHandler(AlreadyLinkAccountException.class)
    public ResponseEntity<MessageDto> handleAlreadyLinkAccountException(
        AlreadyLinkAccountException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageDto(e.getMessage()));
    }
}
