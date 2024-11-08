package shop.S5G.shop.repository.point;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.dto.point.PointSourceView;
import shop.S5G.shop.entity.point.PointSource;

public interface PointSourceRepository extends JpaRepository<PointSource, Long> {
    Optional<PointSource> findBySourceName(String sourceName);
    List<PointSourceView> findAllBy();
}
