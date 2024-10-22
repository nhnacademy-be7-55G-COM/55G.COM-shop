package shop.S5G.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
