package shop.S5G.shop.repository.member.qdsl;

import java.util.Optional;
import shop.S5G.shop.entity.member.Member;

public interface MemberQuerydslRepository {

    Optional<Member> findByIdAndActiveIsTrue(long memberId);
}
