package shop.S5G.shop.controller.member;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.memberStatus.MemberStatusRequestDto;
import shop.S5G.shop.dto.memberStatus.MemberStatusResponseDto;
import shop.S5G.shop.dto.tag.MessageDto;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.service.member.MemberStatusService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class MemberStatusController {

    private final MemberStatusService memberStatusService;

    @GetMapping("/member/status")
    public ResponseEntity<List<MemberStatusResponseDto>> getMemberStatus() {
        List<MemberStatusResponseDto> memberStatusList = memberStatusService.getAllMemberStatus();
        return ResponseEntity.ok(memberStatusList);
    }

    @GetMapping("/member/status/{memberStatusId}")
    public ResponseEntity<MemberStatusResponseDto> getMemberStatusById(
        @PathVariable Long memberStatusId) {
        MemberStatusResponseDto memberStatusResponseDto = memberStatusService.getMemberStatus(
            memberStatusId);
        return ResponseEntity.ok(memberStatusResponseDto);
    }

    @PostMapping("/member/status")
    public ResponseEntity<MessageDto> createMemberStatus(
        @Valid @RequestBody MemberStatusRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("잘못된 요청입니다");
        }
        memberStatusService.saveMemberStatus(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDto("생성 성공"));

    }

    @PutMapping("/member/status/{memberStatusId}")
    public ResponseEntity<MessageDto> updateMemberStatus(@PathVariable Long memberStatusId,
        @Valid @RequestBody MemberStatusRequestDto memberStatusRequestDto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("잘못된 요청입니다");
        }
        memberStatusService.updateMemberStatus(memberStatusId, memberStatusRequestDto);
        return ResponseEntity.ok().body(new MessageDto("변경 성공"));
    }

}
