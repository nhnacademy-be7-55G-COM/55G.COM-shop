package shop.s5g.shop.repository.member.qdsl;

import java.time.LocalDateTime;
import java.util.Optional;
import shop.s5g.shop.entity.member.Member;

public interface MemberQuerydslRepository {

    void updateLatestLoginAt(String loginId, LocalDateTime lastLoginAt);

    Optional<Member> findByIdAndActiveIsTrue(long memberId);
}
