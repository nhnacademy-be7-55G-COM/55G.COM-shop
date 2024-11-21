package shop.s5g.shop.service.book.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookRequestDto;
import shop.s5g.shop.dto.book.BookResponseDto;
import shop.s5g.shop.dto.book.BookSimpleResponseDto;
import shop.s5g.shop.dto.book.category.BookCategoryBookResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookImage;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.bookstatus.BookStatusResourceNotFoundException;
import shop.s5g.shop.exception.publisher.PublisherResourceNotFoundException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.book.category.BookCategoryRepository;
import shop.s5g.shop.repository.book.status.BookStatusRepository;
import shop.s5g.shop.repository.book.image.BookImageRepository;
import shop.s5g.shop.repository.book.status.BookStatusRepository;
import shop.s5g.shop.repository.publisher.PublisherRepository;
import shop.s5g.shop.service.book.BookService;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final BookStatusRepository statusRepository;
    private final BookCategoryRepository bookcategoryRepository;
    private final BookImageRepository bookImageRepository;

    //도서 등록
    public void createBook(BookRequestDto bookDto) {
        Publisher publisher = publisherRepository.findById(bookDto.publisherId())
            .orElseThrow(() -> new PublisherResourceNotFoundException("해당 출판사를 찾을 수 없습니다."));

        BookStatus bookStatus = statusRepository.findById(bookDto.bookStatusId())
            .orElseThrow(() -> new BookStatusResourceNotFoundException("해당 도서 상태를 찾을 수 없습니다."));

        Book book = new Book(
            publisher,
            bookStatus,
            bookDto.title(),
            bookDto.chapter(),
            bookDto.description(),
            LocalDate.parse(bookDto.publishedDate()),
            bookDto.isbn(),
            bookDto.price(),
            bookDto.discountRate(),
            bookDto.isPacked(),
            bookDto.stock(),
            0L,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        book = bookRepository.save(book);

        if(bookDto.thumbnailPath()!=null) {
            BookImage bookImage = bookImageRepository.save(
                new BookImage(book, bookDto.thumbnailPath()));
        }
    }

    //모든 도서 리스트 조회
    public List<BookResponseDto> allBook() {
        return bookRepository.findAllBookList();
    }

    //도서 전체 조회 페이징
//    @Override
//    public Page<BookPageableResponseDto> allBookPageable(Pageable pageable) {
//        return bookRepository.findAllBookPage(pageable);
//    }

    //도서 상세 조회
    public BookDetailResponseDto getBookById(Long bookId) {
        // TODO: 코드 간소화
        if (!bookRepository.existsById(bookId)) {
            throw new BookResourceNotFoundException("Book with id " + bookId + " not found");
        }
        return bookRepository.getBookDetail(bookId);
    }

    //도서 수정
    public void updateBooks(Long bookId, BookRequestDto bookDto) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookResourceNotFoundException("책이 존재하지 않습니다.");
        }
        bookRepository.updateBook(bookId, bookDto);
    }

    //도서 삭제
    public void deleteBooks(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookResourceNotFoundException("Book with id " + bookId + " not found");
        }
        bookRepository.deleteById(bookId);
    }

    @Override
    public List<BookSimpleResponseDto> getSimpleBooks(List<Long> bookIdList) {
        return bookRepository.findSimpleBooksByIdList(bookIdList);
    }

    //도서id 리스트로 도서 리스트 조회
    @Override
    public List<BookDetailResponseDto> getBookListByBookIdList(List<BookCategoryBookResponseDto> bookIdList) {
        List<BookDetailResponseDto> bookList = new ArrayList<>();
        for(int i=0 ; i<bookIdList.size(); i++) {
//            BookDetailResponseDto bookDetail = bookRepository.getBookDetail(bookIdList.get(i).BookId());
//            bookList.add(bookDetail);
        }
        return bookList;
    }

    //categoryId로 bookList조회
//    @Override
//    public List<BookPageableResponseDto> getBookList(Long categoryId) {
//        return bookcategoryRepository.getBookList(categoryId);
//    }
}