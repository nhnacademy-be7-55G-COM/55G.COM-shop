package shop.s5g.shop.repository.member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.repository.member.qdsl.MemberQuerydslRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQuerydslRepository {

    static final String ACTIVE_STATUS = "ACTIVE";
    static final String WITHDRAWAL_STATUS = "WITHDRAWAL";

    boolean existsByLoginIdAndStatus_TypeName(String loginId, String typeName);

    Member findByLoginIdAndStatus_TypeName(String loginId, String typeName);

    List<Member> findByStatus_TypeName(String typeName);

    Optional<Member> findByPaycoIdNo(String paycoIdNo);

    boolean existsByLoginId(String loginId);

    boolean existsByPaycoIdNo(String paycoIdNo);

    Optional<Member> findByIdAndStatus_TypeName(long memberId, String typeName);
}
