package shop.S5G.shop.repository.member.qdsl.impl;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.entity.member.MemberGrade;
import shop.S5G.shop.repository.member.qdsl.MemberGradeQuerydslRepository;

import static shop.S5G.shop.entity.member.QMemberGrade.memberGrade;

public class MemberGradeQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    MemberGradeQuerydslRepository {

    public MemberGradeQuerydslRepositoryImpl() {
        super(MemberGrade.class);
    }

    @Override
    public void updateMemberGrade(long gradeId, String gradeName, int condition, int point) {
        update(memberGrade)
                .set(memberGrade.gradeName, gradeName)
                .set(memberGrade.gradeCondition, condition)
                .set(memberGrade.point, point)
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
