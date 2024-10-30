package shop.S5G.shop.repository.bookcategory.qdsl.impl;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.bookcategory.BookCategoryResponseDto;
import shop.S5G.shop.entity.bookcategory.BookCategory;
import shop.S5G.shop.repository.bookcategory.qdsl.BookCategoryQuerydslRepository;

import java.util.List;

import static shop.S5G.shop.entity.bookcategory.QBookCategory.bookCategory;

public class BookCategoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookCategoryQuerydslRepository {

    public BookCategoryQuerydslRepositoryImpl() {
        super(BookCategory.class);
    }

    //bookId에 해당하는 categoryId 리스트 리턴
    @Override
    public List<BookCategoryResponseDto> findByIdBookId(Long bookId) {
        update(bookCategory);

        return List.of();
    }
}
