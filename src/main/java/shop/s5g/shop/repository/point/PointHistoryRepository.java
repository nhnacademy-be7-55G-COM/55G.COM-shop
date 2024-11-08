package shop.s5g.shop.repository.point;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.point.PointHistory;
import shop.s5g.shop.repository.point.qdsl.PointHistoryQuerydslRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>,
    PointHistoryQuerydslRepository {
    Optional<PointHistory> findByIdAndActiveIsTrue(long id);
}
