package shop.S5G.shop.service.book.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.Book.BookRequestDto;
import shop.S5G.shop.dto.Book.BookResponseDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.exception.BookException.BookResourceNotFoundException;
import shop.S5G.shop.repository.book.BookRepository;
import shop.S5G.shop.service.book.BookService;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    //도서 등록
    public void createBook(BookRequestDto bookDto) {
        Book book = new Book(
                bookDto.publisherId(),
                bookDto.bookStatusId(),
                bookDto.title(),
                bookDto.chapter(),
                bookDto.description(),
                bookDto.publishedDate(),
                bookDto.isbn(),
                bookDto.price(),
                bookDto.discountRate(),
                bookDto.isPacked(),
                bookDto.stock(),
                bookDto.views(),
                bookDto.createdAt());
        bookRepository.save(book);
    }

    //모든 도서 리스트 조회
    public List<BookResponseDto> allBook() {
        return bookRepository.findAllBookList();
    }

    //도서 상세 조회
    public BookResponseDto getBookById(Long bookId) {
        if(!bookRepository.existsById(bookId)) {
             throw new BookResourceNotFoundException("Book with id " + bookId + " not found");
        }
        return bookRepository.getBookDetail(bookId);
    }

    //도서 수정
    public void updateBooks(Long bookId, BookRequestDto bookDto){
        if (!bookRepository.existsById(bookId)) {
            throw new BookResourceNotFoundException(bookId + " 도서는 존재하지 않습니다.");
        }
        bookRepository.updateBook(bookId, bookDto);
    }

    //도서 삭제
    public void deleteBooks(Long bookId) {
        if(!bookRepository.existsById(bookId)) {
            throw new BookResourceNotFoundException("Book with id " + bookId + " not found");
        }
        bookRepository.deleteById(bookId);
    }

    //도서 아이디 list로 도서 조회
    public List<BookResponseDto> findAllByBookIds(List<Long> bookIds) {
        for(int i=0 ; i<bookIds.size(); i++) {
            if(!bookRepository.existsById(bookIds.get(i))) {
            throw new BookResourceNotFoundException(+ bookIds.get(i) + " 도서는 존재하지 않습니다.");
            }
        }
        return bookRepository.findAllByBookIdIn(bookIds);
    }

}
