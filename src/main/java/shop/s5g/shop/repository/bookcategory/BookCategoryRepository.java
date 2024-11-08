package shop.s5g.shop.repository.bookcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.bookcategory.BookCategory;
import shop.s5g.shop.entity.bookcategory.BookCategoryId;
import shop.s5g.shop.repository.bookcategory.qdsl.BookCategoryQuerydslRepository;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId>, BookCategoryQuerydslRepository {

}






