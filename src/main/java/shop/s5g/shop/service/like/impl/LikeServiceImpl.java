package shop.s5g.shop.service.like.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.book.BookLikeResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.like.Like;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.like.LikeAlreadyExistsException;
import shop.s5g.shop.exception.like.LikeBadRequestException;
import shop.s5g.shop.exception.member.CustomerNotFoundException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.like.LikeRepository;
import shop.s5g.shop.repository.member.CustomerRepository;
import shop.s5g.shop.service.like.LikeService;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;

    //도서 내 좋아요 등록
    @Override
    public void addLikeInBook(Long customerId, Long bookId) {
        if (customerId == null) {
            throw new CustomerNotFoundException("로그인이 필요합니다.");
        }

        if (customerId < 1) {
            throw new LikeBadRequestException("customer id는 1보다 커야 합니다.");
        }

        if (bookId < 1) {
            throw new LikeBadRequestException("book id는 1보다 커야 합니다.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("고객을 찾을 수 없습니다."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookResourceNotFoundException("도서를 찾을 수 없습니다."));

        //좋아요 갯수 증가
        bookRepository.likeCount(bookId);

        Like like = new Like(customer, book);
//        likeRepository.addLike(customer, book);

        if(likeRepository.findById(like.getId()).isPresent()) {
            throw new LikeAlreadyExistsException("좋아요가 등록된 도서 입니다!");
        }

        likeRepository.save(like);
        likeRepository.flush();
    }

    //도서 내 좋아요 삭제
    @Override
    @Transactional // 트랜잭션 설정
    public void deleteLikeInBook(Long customerId, Long bookId) {
        if (customerId < 1) {
            throw new LikeBadRequestException("customer id는 1보다 커야 합니다.");
        }

        if (bookId < 1) {
            throw new LikeBadRequestException("book id는 1보다 커야 합니다.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("고객을 찾을 수 없습니다."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookResourceNotFoundException("도서를 찾을 수 없습니다."));

        likeRepository.deleteLike(customer, book);
//        likeRepository.delete()
    }


    //마이페이지에서 좋아요 누른 도서 조회
    @Override
    public List<BookLikeResponseDto> getLikeBookByCustomerId(Long customerId) {
        List<BookLikeResponseDto> books = likeRepository.getLikeBooksByCustomerId(customerId);
        return books;
    }
}
