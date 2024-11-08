package shop.s5g.shop.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.repository.category.qdsl.CategoryQuerydslRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryQuerydslRepository {
}
