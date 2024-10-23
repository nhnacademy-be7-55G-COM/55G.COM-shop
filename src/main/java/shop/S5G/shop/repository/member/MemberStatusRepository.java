package shop.S5G.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.member.MemberStatus;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long> {
}
