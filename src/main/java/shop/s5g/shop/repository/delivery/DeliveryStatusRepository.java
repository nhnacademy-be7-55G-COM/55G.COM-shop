package shop.s5g.shop.repository.delivery;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.dto.delivery.DeliveryStatusView;
import shop.s5g.shop.entity.delivery.DeliveryStatus;
import shop.s5g.shop.exception.EssentialDataNotFoundException;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Long> {
    Optional<DeliveryStatus> findByName(String name);
    default DeliveryStatus findStatusByName(String name) {
        return findByName(name).orElseThrow(() -> new EssentialDataNotFoundException(name+ " 상태가 존재하지 않습니다"));
    }
    default DeliveryStatus findStatusByName(DeliveryStatus.Type type) {
        return findStatusByName(type.name());
    }

    List<DeliveryStatusView> findAllBy();
}
