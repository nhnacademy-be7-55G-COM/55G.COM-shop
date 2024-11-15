package shop.s5g.shop.repository.cart;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import shop.s5g.shop.entity.cart.Cart;
import shop.s5g.shop.entity.cart.CartPk;

public interface CartRepository extends JpaRepository<Cart, CartPk> {

    List<Cart> findAllByCartPk_CustomerId(Long customerId);

    void deleteAllByCartPk_CustomerId(Long customerId);
}
