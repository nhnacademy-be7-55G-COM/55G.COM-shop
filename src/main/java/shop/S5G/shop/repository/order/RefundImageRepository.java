package shop.S5G.shop.repository.order;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.dto.refund.RefundImageView;
import shop.S5G.shop.entity.refund.RefundImage;

public interface RefundImageRepository extends JpaRepository<RefundImage, Long> {
    List<RefundImageView> queryAllByRefundHistoryId(long refundHistoryId);
}
