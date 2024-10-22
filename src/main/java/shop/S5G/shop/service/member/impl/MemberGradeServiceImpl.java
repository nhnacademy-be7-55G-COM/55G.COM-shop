package shop.S5G.shop.service.member.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.memberGrade.MemberGradeRequestDto;
import shop.S5G.shop.entity.member.MemberGrade;
import shop.S5G.shop.exception.member.MemberGradeAlreadyExistsException;
import shop.S5G.shop.exception.member.MemberGradeNotFoundException;
import shop.S5G.shop.repository.member.MemberGradeRepository;
import shop.S5G.shop.service.member.MemberGradeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberGradeServiceImpl implements MemberGradeService {

    private final MemberGradeRepository memberGradeRepository;

    @Override
    public void addGrade(MemberGradeRequestDto grade) {
        if (existsGradeByName(grade.getGradeName())){
            throw new MemberGradeAlreadyExistsException(grade.getGradeName() + "이 이미 존재합니다.");
        }

        MemberGrade memberGrade = new MemberGrade(grade.getGradeName(), grade.getGradeCondition(), grade.getPoint(), true);
        memberGradeRepository.save(memberGrade);
    }

    @Override
    public void updateGrade(long gradeId, MemberGradeRequestDto grade) {
        if (!existsGradeByName(grade.getGradeName())){
            throw new MemberGradeNotFoundException(grade.getGradeName() + "이 존재하지 않습니다");
        }

        memberGradeRepository.updateMemberGrade(gradeId, grade.getGradeName(), grade.getGradeCondition(), grade.getPoint());
    }

    @Transactional(readOnly = true)
    @Override
    public MemberGrade getGradeByName(String name) {
        if (!existsGradeByName(name)){
            throw new MemberGradeNotFoundException(name + "이 존재하지 않습니다.");
        }
        return memberGradeRepository.findByGradeName(name);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberGrade> getAllGrades() {
        return memberGradeRepository.findAll();
    }

    @Override
    public void deleteGrade(long gradeId) {
        if (!existsGradeById(gradeId)){
            throw new MemberGradeNotFoundException("등급이 존재하지 않습니다.");
        }

        memberGradeRepository.inactiveMemberGrade(gradeId);
    }

    @Override
    public boolean existsGradeByName(String name) {
        return memberGradeRepository.existsByGradeNameAndActive(name, true);
    }

    @Override
    public boolean existsGradeById(long gradeId) {
        return memberGradeRepository.existsById(gradeId);
    }
}
