package shop.S5G.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.tag.MessageDto;
import shop.S5G.shop.dto.member.MemberRegistrationRequestDto;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.service.member.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<MessageDto> registerMember(@Valid @RequestBody
    MemberRegistrationRequestDto memberRegistrationRequestDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }
        memberService.saveMember(memberRegistrationRequestDto);
        return ResponseEntity.ok(new MessageDto("생성 성공"));
    }

}
