package shop.s5g.shop.repository.order;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.exception.EssentialDataNotFoundException;

public interface OrderDetailTypeRepository extends JpaRepository<OrderDetailType, Long> {
    Optional<OrderDetailType> findByName(String name);
    default OrderDetailType findStatusByName(String name){
        return findByName(name).orElseThrow(() -> new EssentialDataNotFoundException(name + "주문 타입이 존재하지 않습니다."));
    }
    default OrderDetailType findStatusByName(OrderDetailType.Type type){
        return findStatusByName(type.name());
    }
}
