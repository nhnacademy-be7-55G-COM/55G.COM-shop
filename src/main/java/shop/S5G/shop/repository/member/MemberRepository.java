package shop.S5G.shop.repository.member;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.member.Member;
import shop.S5G.shop.repository.member.qdsl.MemberQuerydslRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQuerydslRepository {

    boolean existsByLoginIdAndStatus_TypeName(String loginId, String typeName);

    Member findByLoginIdAndStatus_TypeName(String loginId, String typeName);

    List<Member> findByStatus_TypeName(String typeName);

    boolean existsByLoginId(String loginId);

}
