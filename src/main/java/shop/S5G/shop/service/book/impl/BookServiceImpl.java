package shop.S5G.shop.service.book.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.Book.BookRequestDto;
import shop.S5G.shop.dto.Book.BookResponseDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.exception.BookException.BookResourceNotFoundException;
import shop.S5G.shop.repository.BookRepository;
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
                bookDto.getPublisherId(),
                bookDto.getBookStatusId(),
                bookDto.getTitle(),
                bookDto.getChapter(),
                bookDto.getDescription(),
                bookDto.getPublishedDate(),
                bookDto.getIsbn(),
                bookDto.getPrice(),
                bookDto.getDiscountRate(),
                bookDto.isPacked(),
                bookDto.getStock(),
                bookDto.getViews(),
                bookDto.getCreatedAt());
        bookRepository.save(book);
    }

    //모든 도서 리스트 조회
    public List<BookResponseDto> allBook() {
        List<Book> bookList = bookRepository.findAll();
        List<BookResponseDto> bookResponseDtoList = new ArrayList<BookResponseDto>();
        for(int i=0 ; i<bookList.size() ; i++) {
            bookResponseDtoList.get(i).setPublisherId(bookList.get(i).getPublisherId());
            bookResponseDtoList.get(i).setBookStatusId(bookList.get(i).getBookStatusId());
            bookResponseDtoList.get(i).setTitle(bookList.get(i).getTitle());
            bookResponseDtoList.get(i).setChapter(bookList.get(i).getChapter());
            bookResponseDtoList.get(i).setDescription(bookList.get(i).getDescription());
            bookResponseDtoList.get(i).setPublishedDate(bookList.get(i).getPublishedDate());
            bookResponseDtoList.get(i).setIsbn(bookList.get(i).getIsbn());
            bookResponseDtoList.get(i).setPrice(bookList.get(i).getPrice());
            bookResponseDtoList.get(i).setDiscountRate(bookList.get(i).getDiscountRate());
            bookResponseDtoList.get(i).setPacked(bookList.get(i).isPacked());
            bookResponseDtoList.get(i).setStock(bookList.get(i).getStock());
            bookResponseDtoList.get(i).setViews(bookList.get(i).getViews());
            bookResponseDtoList.get(i).setCreatedAt(bookList.get(i).getCreatedAt());
        }
        return bookResponseDtoList;
    }

    //도서 상세 조회
    public BookResponseDto getBookById(Long bookId) {
        if(!bookRepository.existsById(bookId)) {
             throw new BookResourceNotFoundException("Book with id " + bookId + " not found");
        }
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookResourceNotFoundException("해당 도서가 존재하지 않습니다."));
        BookResponseDto bookResponseDto = new BookResponseDto();
        bookResponseDto.setPublisherId(book.getPublisherId());
        bookResponseDto.setBookStatusId(book.getBookStatusId());
        bookResponseDto.setTitle(book.getTitle());
        bookResponseDto.setChapter(book.getChapter());
        bookResponseDto.setDescription(book.getDescription());
        bookResponseDto.setPublishedDate(book.getPublishedDate());
        bookResponseDto.setIsbn(book.getIsbn());
        bookResponseDto.setPrice(book.getPrice());
        bookResponseDto.setDiscountRate(book.getDiscountRate());
        bookResponseDto.setPacked(book.isPacked());
        bookResponseDto.setStock(book.getStock());
        bookResponseDto.setViews(book.getViews());
        bookResponseDto.setCreatedAt(book.getCreatedAt());
        return bookResponseDto;
    }

    //도서 수정
    public void updateBooks(Long bookId, BookRequestDto bookDto) {
        Book books = bookRepository.findById(bookId).orElseThrow(() -> new BookResourceNotFoundException("Book with id " + bookId + " not found"));

        Book book = new Book(
                bookDto.getPublisherId(),
                bookDto.getBookStatusId(),
                bookDto.getTitle(),
                bookDto.getChapter(),
                bookDto.getDescription(),
                bookDto.getPublishedDate(),
                bookDto.getIsbn(),
                bookDto.getPrice(),
                bookDto.getDiscountRate(),
                bookDto.isPacked(),
                bookDto.getStock(),
                bookDto.getViews(),
                bookDto.getCreatedAt());

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
