package shop.s5g.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.member.MemberStatus;
import shop.s5g.shop.repository.member.qdsl.MemberStatusQuerydslRepository;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long>,
    MemberStatusQuerydslRepository {

    MemberStatus findByTypeName(String typeName);

    boolean existsByTypeName(String typeName);
}
