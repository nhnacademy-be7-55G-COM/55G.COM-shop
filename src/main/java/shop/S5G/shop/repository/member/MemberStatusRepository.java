package shop.S5G.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.member.MemberStatus;
import shop.S5G.shop.repository.member.qdsl.MemberStatusQuerydslRepository;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long>,
    MemberStatusQuerydslRepository {

    boolean existsByTypeName(String typeName);
}
