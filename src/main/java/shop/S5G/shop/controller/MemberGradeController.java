package shop.S5G.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.S5G.shop.dto.MessageDto;
import shop.S5G.shop.dto.memberGrade.MemberGradeRequestDto;
import shop.S5G.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.service.member.MemberGradeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
// TODO PathVariable 로 받을지 param으로 받을지 생각해보기
public class MemberGradeController {

    private final MemberGradeService memberGradeService;

    @GetMapping("/member/grade")
    public ResponseEntity<List<MemberGradeResponseDto>> getMemberGradeList() {
        List<MemberGradeResponseDto> responseDtoList = memberGradeService.getActiveGrades();
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/member/grade/{gradeId}")
    public ResponseEntity<MemberGradeResponseDto> getMemberGrade(@PathVariable long gradeId) {
        MemberGradeResponseDto responseDto = memberGradeService.getGradeById(gradeId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/member/grade")
    public ResponseEntity<MessageDto> createMemberGrade(@Valid @RequestBody MemberGradeRequestDto requestDto,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException("잘못된 요청입니다");
        }
        memberGradeService.addGrade(requestDto);
        return ResponseEntity.status(201).body(new MessageDto("등급 생성 성공"));
    }

    @PutMapping("/member/grade/{gradeId}")
    public ResponseEntity<MessageDto> updateMemberGrade(@Valid @RequestBody MemberGradeRequestDto requestDto,
                                                        BindingResult bindingResult,
                                                        @PathVariable long gradeId) {
        if (gradeId <= 0 || bindingResult.hasErrors()) {
            throw new BadRequestException("잘못된 요청입니다");
        }
        memberGradeService.updateGrade(gradeId, requestDto);

        return ResponseEntity.ok(new MessageDto("변경 성공"));
    }

    @DeleteMapping("/member/grade/{gradeId}")
    public ResponseEntity<MessageDto> deleteMemberGrade(@PathVariable long gradeId) {
        if (gradeId <= 0) {
            throw new BadRequestException("잘못된 요청입니다");
        }
        memberGradeService.deleteGrade(gradeId);
        return ResponseEntity.ok(new MessageDto("삭제 성공"));
    }
}
