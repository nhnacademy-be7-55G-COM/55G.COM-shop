//package shop.s5g.shop.service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import shop.s5g.shop.dto.book.BookDetailResponseDto;
//import shop.s5g.shop.dto.book.BookRequestDto;
//import shop.s5g.shop.dto.bookAuthor.BookAuthorResponseDto;
//import shop.s5g.shop.entity.Author;
//import shop.s5g.shop.entity.AuthorType;
//import shop.s5g.shop.entity.Book;
//import shop.s5g.shop.entity.BookAuthor;
//import shop.s5g.shop.entity.BookStatus;
//import shop.s5g.shop.entity.Publisher;
//import shop.s5g.shop.exception.book.BookAlreadyExistsException;
//import shop.s5g.shop.exception.book.BookResourceNotFoundException;
//import shop.s5g.shop.repository.AuthorRepository;
//import shop.s5g.shop.repository.AuthorTypeRepository;
//import shop.s5g.shop.repository.BookAuthorRepository;
//import shop.s5g.shop.repository.book.BookRepository;
//import shop.s5g.shop.repository.bookstatus.BookStatusRepository;
//import shop.s5g.shop.repository.PublisherRepository;
//
//@Service
//@RequiredArgsConstructor
//public class BookServiceImpl implements BookService {
//
//    private final BookRepository bookRepository;
//    private final PublisherRepository publisherRepository;
//    private final BookStatusRepository bookStatusRepository;
//    private final BookAuthorRepository bookAuthorRepository;
//    private final AuthorTypeRepository authorTypeRepository;
//    private final AuthorRepository authorRepository;
//
//    //도서 등록
//    public void createBook(BookRequestDto bookRequestDto) {
//        Publisher publisher=publisherRepository.findById(bookRequestDto.publisherId()).get();
//        BookStatus bookStatus=bookStatusRepository.findById(bookRequestDto.bookStatusId()).get();
//
//        Book book = new Book(
//            publisher,
//            bookStatus,
//            bookRequestDto.title(),
//            bookRequestDto.chapter(),
//            bookRequestDto.description(),
//            LocalDate.parse(bookRequestDto.publishedDate()).atStartOfDay(),
//            bookRequestDto.isbn(),
//            bookRequestDto.price(),
//            bookRequestDto.discountRate(),
//            bookRequestDto.isPacked(),
//            bookRequestDto.stock(),
//            0L,
//            LocalDateTime.now());
//        bookRepository.save(book);
//    }
//
//    //모든 도서 리스트 조회
////    public List<Book> allBook() {
////        return bookRepository.findAll();
////    }
//    public List<BookDetailResponseDto> allBook() {
//        List<Book> bookList = bookRepository.findAll();
//        List<BookDetailResponseDto> bookDtoList = new ArrayList<>();
//
//        for (Book book : bookList) {
////            Publisher publisher=publisherRepository.findById(book.getPublisherId()).get();
//            BookStatus bookStatus = bookStatusRepository.findById(book.getBookStatus().getId()).get();
//
//            List<BookAuthor> bookAuthors = bookAuthorRepository.findAllByBook(book);
//            List<BookAuthorResponseDto> bookAuthorResponseDtoList = new ArrayList<>();
//            for (BookAuthor bookAuthor : bookAuthors) {
//                Author author = bookAuthor.getAuthor();
//                AuthorType authorType = bookAuthor.getAuthorType();
//                bookAuthorResponseDtoList.add(
//                    new BookAuthorResponseDto(author.getAuthorId(), author.getName(),
//                        authorType.getAuthorTypeId(), authorType.getTypeName()));
//            }
//
//            bookDtoList.add(BookDetailResponseDto.builder()
//                .bookId(book.getBookId())
////            .publisherName(publisher.getPublisherName())
//                .typeName(bookStatus.getName())
//                .title(book.getTitle())
//                .chapter(book.getChapter())
//                .description(book.getDescription())
//                .publishedDate(book.getPublishedDate())
//                .isbn(book.getIsbn())
//                .price(book.getPrice())
//                .discountRate(book.getDiscountRate())
//                .isPacked(book.isPacked())
//                .stock(book.getStock())
//                .views(book.getViews())
//                .createdAt(book.getCreatedAt())
//                .authorList(bookAuthorResponseDtoList)
//                .build());
//        }
//
//        return bookDtoList;
//    }
//
//    //도서 상세 조회
//    public BookDetailResponseDto getBookById(Long bookId) {
//        if (!bookRepository.existsById(bookId)) {
//            throw new BookResourceNotFoundException("Book with id " + bookId + " not found");
//        }
//        Book book = bookRepository.findById(bookId).get();
//
////        Publisher publisher=publisherRepository.findById(book.getPublisherId()).get();
//        BookStatus bookStatus = bookStatusRepository.findById(book.getBookStatus().getId()).get();
//
//        List<BookAuthor> bookAuthors = bookAuthorRepository.findAllByBook(book);
//        List<BookAuthorResponseDto> bookAuthorResponseDtoList = new ArrayList<>();
//        for (BookAuthor bookAuthor : bookAuthors) {
//            Author author = bookAuthor.getAuthor();
//            AuthorType authorType = bookAuthor.getAuthorType();
//            bookAuthorResponseDtoList.add(
//                new BookAuthorResponseDto(author.getAuthorId(), author.getName(),
//                    authorType.getAuthorTypeId(), authorType.getTypeName()));
//        }
//
//        return BookDetailResponseDto.builder()
//            .bookId(book.getBookId())
////            .publisherName(publisher.getPublisherName())
//            .typeName(bookStatus.getName())
//            .title(book.getTitle())
//            .chapter(book.getChapter())
//            .description(book.getDescription())
//            .publishedDate(book.getPublishedDate())
//            .isbn(book.getIsbn())
//            .price(book.getPrice())
//            .discountRate(book.getDiscountRate())
//            .isPacked(book.isPacked())
//            .stock(book.getStock())
//            .views(book.getViews())
//            .createdAt(book.getCreatedAt())
//            .authorList(bookAuthorResponseDtoList)
//            .build();
//    }
//
//    //도서 수정
//    public void updateBooks(Long bookId, BookRequestDto book) {
//        Optional<Book> oldBookOptional=bookRepository.findById(bookId);
//        Publisher publisher=publisherRepository.findById(book.publisherId()).get();
//        BookStatus bookStatus=bookStatusRepository.findById(book.bookStatusId()).get();
//
//        if(oldBookOptional.isEmpty()){
//            throw new BookResourceNotFoundException("book is not found");
//        }
//        Book oldBook=oldBookOptional.get();
//        Book newBook=Book.builder()
//                .bookId(bookId)
//                .publisher(publisher)
//                .bookStatus(bookStatus)
//                .title(book.title())
//                .chapter(book.chapter())
//                .description(book.description())
//                .publishedDate(LocalDate.parse(book.publishedDate()).atStartOfDay())
//                .isbn(book.isbn())
//                .price(book.price())
//                .discountRate(book.discountRate())
//                .isPacked(book.isPacked())
//                .stock(book.stock())
//                .views(oldBook.getViews())
//                .createdAt(oldBook.getCreatedAt())
//                .build();
//
//        bookRepository.save(newBook);
//    }
//
//    //도서 삭제
//    public void deleteBooks(Long bookId) {
//        if (!bookRepository.existsById(bookId)) {
//            throw new BookResourceNotFoundException("Book with id " + bookId + " not found");
//        }
//        bookRepository.deleteById(bookId);
//    }
//
//    //도서 아이디 list로 도서 조회
//    public List<Book> findAllByBookIds(List<Long> bookIds) {
//
////        return bookRepository.findAllByBookIdIn(bookIds);
//        return List.of();
//    }
//}
