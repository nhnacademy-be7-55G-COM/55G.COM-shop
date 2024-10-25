package shop.S5G.shop.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;

import shop.S5G.shop.entity.cart.Cart;
import shop.S5G.shop.entity.cart.CartPk;

public interface CartRepository extends JpaRepository<Cart, CartPk> {
}
