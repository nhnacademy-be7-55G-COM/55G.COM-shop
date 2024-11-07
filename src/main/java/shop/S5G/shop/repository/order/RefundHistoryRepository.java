package shop.S5G.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.refund.RefundHistory;
import shop.S5G.shop.repository.order.qdsl.RefundHistoryQuerydslRepository;

public interface RefundHistoryRepository extends JpaRepository<RefundHistory, Long>,
    RefundHistoryQuerydslRepository {
}
