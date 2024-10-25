package shop.S5G.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
