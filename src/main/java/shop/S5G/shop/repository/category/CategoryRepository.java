package shop.S5G.shop.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.repository.category.qdsl.CategoryQuerydslRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryQuerydslRepository {
}
