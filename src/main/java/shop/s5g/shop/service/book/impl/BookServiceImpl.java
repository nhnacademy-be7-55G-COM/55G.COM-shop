package shop.s5g.shop.service.book.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.Book.BookRequestDto;
import shop.s5g.shop.dto.Book.BookResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.bookstatus.BookStatusResourceNotFoundException;
import shop.s5g.shop.exception.publisher.PublisherResourceNotFoundException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.bookstatus.BookStatusRepository;
import shop.s5g.shop.repository.publisher.PublisherRepository;
import shop.s5g.shop.service.book.BookService;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final BookStatusRepository statusRepository;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, PublisherRepository publisherRepository, BookStatusRepository statusRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.statusRepository = statusRepository;
    }

    //도서 등록
    public void createBook(BookRequestDto bookDto) {
//        if (publisherRepository.existsById(bookDto.publisherId().getId())) {
//            throw new PublisherResourceNotFoundException("도서에 해당하는 출판사가 존재하지 않습니다.");
//        }
        Publisher publisher = publisherRepository.findById(bookDto.publisherId()).orElseThrow(() -> new PublisherResourceNotFoundException("해당 출판사를 찾을 수 없습니다."));

//        if(statusRepository.existsById(bookDto.bookStatusId().getId())) {
//            throw new BookStatusResourceNotFoundException("선택하신 도서 상태는 존재하지 않습니다.");
//        }
        BookStatus bookStatus = statusRepository.findById(bookDto.bookStatusId()).orElseThrow(() -> new BookStatusResourceNotFoundException("해당 도서 상태를 찾을 수 없습니다."));

        Book book = new Book(
                publisher,
                bookStatus,
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
}