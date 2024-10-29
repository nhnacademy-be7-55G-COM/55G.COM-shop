package shop.S5G.shop.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.exception.BookException.BookAlreadyExistsException;
import shop.S5G.shop.exception.BookException.BookBadRequestException;
import shop.S5G.shop.exception.BookException.BookResourceNotFoundException;
import shop.S5G.shop.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    //도서 등록
    public void createBook(Book book) {
        Optional<Book> id = bookRepository.findById(book.getBookId());
        if (id.isPresent()) {
            throw new BookAlreadyExistsException("Book with id " + book.getBookId() + " already exists");
        }
        bookRepository.save(book);
    }

    //모든 도서 리스트 조회
    public List<Book> allBook() {
        return bookRepository.findAll();
    }

    //도서 상세 조회
    public Book getBookById(Long bookId) {
        if(!bookRepository.existsById(bookId)) {
             throw new BookResourceNotFoundException("Book with id " + bookId + " not found");
        }
        return bookRepository.findById(bookId).get();
    }

    //도서 수정
    public void updateBooks(Long bookId, Book book) {
        Book books = bookRepository.findById(bookId).orElseThrow(() -> new BookResourceNotFoundException("Book with id " + bookId + " not found"));

        books.setPublisherId(book.getPublisherId());
        books.setBookStatusId(book.getBookStatusId());
        books.setTitle(book.getTitle());
        books.setChapter(book.getChapter());
        books.setDescription(book.getDescription());
        books.setPublishedDate(book.getPublishedDate());
        books.setIsbn(book.getIsbn());
        books.setPrice(book.getPrice());
        books.setDiscountRate(book.getDiscountRate());
        books.setPacked(book.isPacked());
        books.setStock(book.getStock());
        books.setViews(book.getViews());
        books.setCreatedAt(book.getCreatedAt());

        bookRepository.save(books);
    }

    //도서 삭제
    public void deleteBooks(Long bookId) {
        if(!bookRepository.existsById(bookId)) {
            throw new BookResourceNotFoundException("Book with id " + bookId + " not found");
        }
        bookRepository.deleteById(bookId);
    }

    //도서 아이디 list로 도서 조회
    public List<Book> findAllByBookIds(List<Long> bookIds) {

        return bookRepository.findAllByBookIdIn(bookIds);
    }
}
