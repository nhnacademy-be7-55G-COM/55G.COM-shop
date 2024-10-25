package shop.S5G.shop.repository.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.order.WrappingPaper;

public interface WrappingPaperRepository extends JpaRepository<WrappingPaper, Long> {
    List<WrappingPaper> queryByActive(boolean active);
}
