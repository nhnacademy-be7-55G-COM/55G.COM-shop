package shop.s5g.shop.repository.member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.repository.member.qdsl.MemberQuerydslRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQuerydslRepository {
  
    static final String ACTIVE_STATUS = "ACTIVE";

    boolean existsByLoginIdAndStatus_TypeName(String loginId, String typeName);

    Member findByLoginIdAndStatus_TypeName(String loginId, String typeName);

    List<Member> findByStatus_TypeName(String typeName);

    // TODO: active 체크 필요함.
    boolean existsByLoginId(String loginId);

    Optional<Member> findByIdAndStatus_TypeName(long memberId, String typeName);
}
