package shop.S5G.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.member.MemberGrade;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Long> {
}
