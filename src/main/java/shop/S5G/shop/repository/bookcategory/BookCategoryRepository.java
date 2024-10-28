package shop.S5G.shop.repository.bookcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.dto.bookcategory.BookCategoryResponseDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.BookCategory.BookCategory;
import shop.S5G.shop.entity.BookCategory.BookCategoryId;
import shop.S5G.shop.repository.bookcategory.qdsl.BookCategoryQuerydslRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, BookCategoryId>, BookCategoryQuerydslRepository {
    List<Long> findCategoryIdByIdBookId(Long bookId);
}






