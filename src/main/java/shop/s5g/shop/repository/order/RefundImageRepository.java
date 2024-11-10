package shop.s5g.shop.repository.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.dto.refund.RefundImageView;
import shop.s5g.shop.entity.refund.RefundImage;

public interface RefundImageRepository extends JpaRepository<RefundImage, Long> {
    List<RefundImageView> queryAllByRefundHistoryId(long refundHistoryId);
}
