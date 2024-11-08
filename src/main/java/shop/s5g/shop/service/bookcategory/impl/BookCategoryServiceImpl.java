package shop.s5g.shop.service.bookcategory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.bookcategory.BookCategoryResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.bookcategory.BookCategory;
import shop.s5g.shop.entity.bookcategory.BookCategoryId;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.category.CategoryResourceNotFoundException;
import shop.s5g.shop.exception.bookcategory.BookCategoryBadRequestException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.bookcategory.BookCategoryRepository;
import shop.s5g.shop.service.bookcategory.BookCategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {

    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    //도서 내 카테고리 등록
    @Override
    public void addCategoryInBook(Long bookId, Long categoryId) {

        if (bookId < 1) {
            throw new BookCategoryBadRequestException("bookId는 1보다 커야 합니다.");
        }
        if (categoryId < 1) {
            throw new BookCategoryBadRequestException("categoryId는 1보다 커야 합니다");
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookResourceNotFoundException("없는 도서 입니다."));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryResourceNotFoundException("없는 카테고리 입니다."));

        BookCategory bookCategory = new BookCategory(new BookCategoryId(bookId, categoryId), category, book);

        bookCategoryRepository.save(bookCategory);
    }

    //도서 내 카테고리 조회
    @Override
    public List<BookCategoryResponseDto> getCategoryInBook(Long bookId) {

        if (bookId < 1) {
            throw new BookCategoryBadRequestException("bookId는 1보다 커야 합니다.");
        }

        //bookId로 categoryId 리스트 찾기
        return bookCategoryRepository.findCategoryByBookId(bookId);
    }

    //도서 내 카테고리 삭제
    @Override
    public void deleteCategory(Long bookId, Long categoryId) {

        if (bookId < 1) {
            throw new BookCategoryBadRequestException("bookId는 1보다 커야 합니다.");
        }
        if (categoryId < 1) {
            throw new BookCategoryBadRequestException("categoryId는 1보다 커야 합니다");
        }
         //bookId에 해당하는 book이 없을 때
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookResourceNotFoundException("Book not found"));
        //categoryId에 해당하는 category가 없을 때
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryResourceNotFoundException("Category not found"));

        BookCategory bookCategory = new BookCategory(new BookCategoryId(bookId, categoryId), category, book);

        bookCategoryRepository.delete(bookCategory);
    }
}