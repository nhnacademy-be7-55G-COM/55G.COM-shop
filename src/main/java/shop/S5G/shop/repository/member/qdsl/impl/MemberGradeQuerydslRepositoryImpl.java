package shop.S5G.shop.repository.member.qdsl.impl;

import static shop.S5G.shop.entity.member.QMemberGrade.memberGrade;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.memberGrade.MemberGradeRequestDto;
import shop.S5G.shop.entity.member.MemberGrade;
import shop.S5G.shop.repository.member.qdsl.MemberGradeQuerydslRepository;

public class MemberGradeQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    MemberGradeQuerydslRepository {

    public MemberGradeQuerydslRepositoryImpl() {
        super(MemberGrade.class);
    }



    @Override
    public void updateMemberGrade(long gradeId, MemberGradeRequestDto requestDto) {
        update(memberGrade)
            .set(memberGrade.gradeName, requestDto.gradeName())
            .set(memberGrade.gradeCondition, requestDto.gradeCondition())
            .set(memberGrade.point, requestDto.point())
            .where(memberGrade.memberGradeId.eq(gradeId))
            .execute();
    }

    @Override
    public void inactiveMemberGrade(long gradeId) {
        update(memberGrade)
            .set(memberGrade.active, false)
            .where(memberGrade.memberGradeId.eq(gradeId))
            .execute();
    }
}
