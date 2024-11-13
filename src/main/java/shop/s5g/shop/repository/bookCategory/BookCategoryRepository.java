package shop.s5g.shop.repository.bookCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.bookCategory.BookCategory;
import shop.s5g.shop.entity.bookCategory.BookCategoryId;
import shop.s5g.shop.repository.bookCategory.qdsl.BookCategoryQuerydslRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId>, BookCategoryQuerydslRepository {

}






