package shop.S5G.shop.repository.point;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.point.PointHistory;
import shop.S5G.shop.repository.point.qdsl.PointHistoryQuerydslRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long>,
    PointHistoryQuerydslRepository {

}
