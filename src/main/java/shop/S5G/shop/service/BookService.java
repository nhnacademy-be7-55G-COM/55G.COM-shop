package shop.S5G.shop.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    //도서 등록
    public void createBook(Book book) {
        bookRepository.save(book);
    }

    //모든 도서 리스트 조회
    public List<Book> allBook() {
        return bookRepository.findAll();
    }

    //도서 상세 조회
    public Book getBookByid(int bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    //도서 수정
    public void updateBooks(int bookId, Book book) {
        Optional<Book> books = bookRepository.findById(bookId);

        books.get().setBook_id(book.getBook_id());
        books.get().setPublisher_id(book.getPublisher_id());
        books.get().setBook_status_id(book.getBook_status_id());
        books.get().setTitle(book.getTitle());
        books.get().setChapter(book.getChapter());
        books.get().setDescreption(book.getDescreption());
        books.get().setPublished_date(book.getPublished_date());
        books.get().setIsbn(book.getIsbn());
        books.get().setPrice(book.getPrice());
        books.get().setDiscount_rate(book.getDiscount_rate());
        books.get().set_packed(book.is_packed());
        books.get().setStock(book.getStock());
        books.get().setViews(book.getViews());
        books.get().setCreated_at(book.getCreated_at());

        bookRepository.save(books.get());
    }

    public void deleteBooks(int bookId) {
        bookRepository.deleteById(bookId);
    }
}
