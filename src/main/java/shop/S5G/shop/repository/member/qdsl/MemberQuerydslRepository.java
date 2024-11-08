package shop.S5G.shop.repository.member.qdsl;

import java.time.LocalDateTime;

public interface MemberQuerydslRepository {

    void updateLatestLoginAt(String loginId, LocalDateTime lastLoginAt);

}
