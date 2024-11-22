package shop.s5g.shop.controller.member;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.member_grade.MemberGradeRequestDto;
import shop.s5g.shop.dto.member_grade.MemberGradeResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.service.member.MemberGradeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
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
    public ResponseEntity<MessageDto> createMemberGrade(
        @Valid @RequestBody MemberGradeRequestDto requestDto, BindingResult bindingResult)
        throws BadRequestException {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }
        memberGradeService.addGrade(requestDto);
        return ResponseEntity.status(201).body(new MessageDto("등급 생성 성공"));
    }

    @PutMapping("/member/grade/{gradeId}")
    public ResponseEntity<MessageDto> updateMemberGrade(
        @Valid @RequestBody MemberGradeRequestDto requestDto, BindingResult bindingResult,
        @PathVariable long gradeId) {
        if (gradeId <= 0 || bindingResult.hasErrors()) {
            throw new BadRequestException();
        }
        memberGradeService.updateGrade(gradeId, requestDto);

        return ResponseEntity.ok(new MessageDto("변경 성공"));
    }

    @DeleteMapping("/member/grade/{gradeId}")
    public ResponseEntity<MessageDto> deleteMemberGrade(@PathVariable long gradeId) {
        if (gradeId <= 0) {
            throw new BadRequestException();
        }
        memberGradeService.deleteGrade(gradeId);
        return ResponseEntity.ok(new MessageDto("삭제 성공"));
    }
}
