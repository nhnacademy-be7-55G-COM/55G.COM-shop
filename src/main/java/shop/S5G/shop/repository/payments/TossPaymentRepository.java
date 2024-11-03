package shop.S5G.shop.repository.payments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.S5G.shop.entity.Payment;

@Repository("tossPaymentRepository")
public interface TossPaymentRepository extends JpaRepository<Payment, Long> {
}
