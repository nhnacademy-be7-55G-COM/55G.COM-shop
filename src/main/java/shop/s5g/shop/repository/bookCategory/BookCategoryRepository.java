package shop.s5g.shop.repository.bookCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.book.category.BookCategory;
import shop.s5g.shop.entity.book.category.BookCategoryId;
import shop.s5g.shop.repository.bookCategory.qdsl.BookCategoryQuerydslRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId>, BookCategoryQuerydslRepository {

}






