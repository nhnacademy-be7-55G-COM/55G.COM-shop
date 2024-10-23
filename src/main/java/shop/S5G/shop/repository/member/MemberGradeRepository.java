package shop.S5G.shop.repository.member;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import shop.S5G.shop.entity.member.MemberGrade;
import shop.S5G.shop.repository.member.qdsl.MemberGradeQuerydslRepository;

import java.util.List;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Long>, MemberGradeQuerydslRepository {
    boolean existsByGradeNameAndActive(String gradeName, boolean active);
    MemberGrade findByGradeName(String gradeName);
    List<MemberGrade> findByActive(boolean active);
}
