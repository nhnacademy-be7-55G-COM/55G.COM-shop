package shop.s5g.shop.repository.member;


import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.member.MemberGrade;
import shop.s5g.shop.repository.member.qdsl.MemberGradeQuerydslRepository;

import java.util.List;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Long>, MemberGradeQuerydslRepository {
    boolean existsByGradeNameAndActive(String gradeName, boolean active);
    MemberGrade findByGradeName(String gradeName);
    List<MemberGrade> findByActive(boolean active);
}
