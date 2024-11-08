package shop.s5g.shop.repository.member.qdsl;

import java.util.Optional;
import shop.s5g.shop.entity.member.Member;

public interface MemberQuerydslRepository {

    Optional<Member> findByIdAndActiveIsTrue(long memberId);
}
