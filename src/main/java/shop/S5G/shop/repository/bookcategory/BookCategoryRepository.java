package shop.S5G.shop.repository.bookcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.bookcategory.BookCategory;
import shop.S5G.shop.entity.bookcategory.BookCategoryId;
import shop.S5G.shop.repository.bookcategory.qdsl.BookCategoryQuerydslRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId>, BookCategoryQuerydslRepository {
    List<Long> findCategoryIdByIdBookId(Long bookId);
}






