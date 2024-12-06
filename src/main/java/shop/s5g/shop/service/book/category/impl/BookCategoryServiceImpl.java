package shop.s5g.shop.service.book.category.impl;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.book.category.BookCategoryResponseDto;
import shop.s5g.shop.dto.book.category.BookCategoryBookResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.book.category.BookCategory;
import shop.s5g.shop.entity.book.category.BookCategoryId;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.category.CategoryResourceNotFoundException;
import shop.s5g.shop.exception.bookcategory.BookCategoryBadRequestException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.book.category.BookCategoryRepository;
import shop.s5g.shop.service.book.category.BookCategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {

    private final String MSG_BAD_BOOK_ID = "bookId는 1보다 커야 합니다.";
    private final String MSG_BAD_CATEGORY_ID = "categoryId는 1보다 커야 합니다";

    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    //도서 내 카테고리 등록
    @Override
    public void addCategoryInBook(Long bookId, Long categoryId) {

        if (bookId < 1) {
            throw new BookCategoryBadRequestException(MSG_BAD_BOOK_ID);
        }
        if (categoryId < 1) {
            throw new BookCategoryBadRequestException(MSG_BAD_CATEGORY_ID);
        }

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new BookResourceNotFoundException("없는 도서 입니다."));
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryResourceNotFoundException("없는 카테고리 입니다."));

        BookCategory bookCategory = new BookCategory(new BookCategoryId(bookId, categoryId),
            category, book,
            LocalDateTime.now(), LocalDateTime.now());

        bookCategoryRepository.save(bookCategory);
    }

}