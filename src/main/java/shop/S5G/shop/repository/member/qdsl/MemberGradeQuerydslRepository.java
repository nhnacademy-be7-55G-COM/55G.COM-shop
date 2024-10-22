package shop.S5G.shop.repository.member.qdsl;

public interface MemberGradeQuerydslRepository {
    void updateMemberGrade(long gradeId, String gradeName, int condition, int point);
    void inactiveMemberGrade(long gradeId);
}
