package shop.s5g.shop.repository.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.order.WrappingPaper;

public interface WrappingPaperRepository extends JpaRepository<WrappingPaper, Long> {
    List<WrappingPaper> queryByActive(boolean active);
}
