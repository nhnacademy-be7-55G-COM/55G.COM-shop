package shop.s5g.shop.service.member.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.memberGrade.MemberGradeRequestDto;
import shop.s5g.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.s5g.shop.entity.member.MemberGrade;
import shop.s5g.shop.exception.member.MemberGradeAlreadyExistsException;
import shop.s5g.shop.exception.member.MemberGradeNotFoundException;
import shop.s5g.shop.repository.member.MemberGradeRepository;
import shop.s5g.shop.service.member.MemberGradeService;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberGradeServiceImpl implements MemberGradeService {

    private final MemberGradeRepository memberGradeRepository;

    @Override
    public void addGrade(MemberGradeRequestDto grade) {
        if (memberGradeRepository.existsByGradeNameAndActive(grade.gradeName(), true)) {
            throw new MemberGradeAlreadyExistsException(grade.gradeName() + "이 이미 존재합니다.");
        }

        MemberGrade memberGrade = new MemberGrade(grade.gradeName(), grade.gradeCondition(),
            grade.point(), true);
        memberGradeRepository.save(memberGrade);
    }

    @Override
    public void updateGrade(long gradeId, MemberGradeRequestDto grade) {
        if (!memberGradeRepository.existsByGradeNameAndActive(grade.gradeName(), true)) {
            throw new MemberGradeNotFoundException(grade.gradeName() + "이 존재하지 않습니다");
        }

        memberGradeRepository.updateMemberGrade(gradeId, grade);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberGradeResponseDto getGradeDtoByName(String name) {
        if (!memberGradeRepository.existsByGradeNameAndActive(name, true)) {
            throw new MemberGradeNotFoundException(name + "이 존재하지 않습니다.");
        }
        MemberGrade grade = memberGradeRepository.findByGradeName(name);
        return MemberGradeResponseDto.toDto(grade);
    }

    @Override
    public MemberGrade getGradeByName(String name) {
        if (!memberGradeRepository.existsByGradeNameAndActive(name, true)) {
            throw new MemberGradeNotFoundException(name + "이 존재하지 않습니다.");
        }
        return memberGradeRepository.findByGradeName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberGradeResponseDto getGradeById(long id) {
        MemberGrade grade = memberGradeRepository.findById(id)
            .orElseThrow(() -> new MemberGradeNotFoundException("등급이 존재하지 않습니다"));

        return MemberGradeResponseDto.toDto(grade);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberGradeResponseDto> getActiveGrades() {
        return memberGradeRepository.findByActive(true)
            .stream()
            .map(MemberGradeResponseDto::toDto
            )
            .toList();
    }

    @Override
    public void deleteGrade(long gradeId) {
        if (!memberGradeRepository.existsById(gradeId)) {
            throw new MemberGradeNotFoundException("등급이 존재하지 않습니다.");
        }
        memberGradeRepository.inactiveMemberGrade(gradeId);
    }
}
