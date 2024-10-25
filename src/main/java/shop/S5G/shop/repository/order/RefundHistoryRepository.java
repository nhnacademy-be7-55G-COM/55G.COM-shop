package shop.S5G.shop.repository.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.S5G.shop.entity.order.RefundHistory;

public interface RefundHistoryRepository extends JpaRepository<RefundHistory, Long> {
    @Query("select r from RefundHistory r join fetch RefundType")
    List<RefundHistory> queryAllByOrderDetailId(long id);
}
