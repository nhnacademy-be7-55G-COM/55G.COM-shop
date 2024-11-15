package shop.s5g.shop.repository.point;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.dto.point.PointPolicyView;
import shop.s5g.shop.entity.point.PointPolicy;
import shop.s5g.shop.repository.point.qdsl.PointPolicyQuerydslRepository;

public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long>,
    PointPolicyQuerydslRepository {
    Optional<PointPolicyView> findByName(String name);

}
