package shop.s5g.shop.repository.payments;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.entity.Payment;

@Repository("tossPaymentRepository")
public interface TossPaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderRelationId(long orderId);
}
