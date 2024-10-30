package shop.S5G.shop.repository.cart;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import shop.S5G.shop.entity.cart.Cart;
import shop.S5G.shop.entity.cart.CartPk;

public interface CartRepository extends JpaRepository<Cart, CartPk> {

    List<Cart> findAllByCartPk_CustomerId(Long customerId);

    void deleteAllByCartPk_CustomerId(Long customerId);
}
