package shop.s5g.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.refund.RefundHistory;
import shop.s5g.shop.repository.order.qdsl.RefundHistoryQuerydslRepository;

public interface RefundHistoryRepository extends JpaRepository<RefundHistory, Long>,
    RefundHistoryQuerydslRepository {
}
