package shop.s5g.shop.repository.coupon.book;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.coupon.CouponBook;
import shop.s5g.shop.repository.coupon.book.qdsl.CouponBookQuerydslRepository;

public interface CouponBookRepository extends JpaRepository<CouponBook, Long>,
    CouponBookQuerydslRepository {

}
