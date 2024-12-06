package shop.s5g.shop.controller.book;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shop.s5g.shop.dto.book.BookLikeResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.like.LikeService;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    //좋아요 등록
    @PostMapping("/like/{bookId}")
    public ResponseEntity<MessageDto> addLike(@PathVariable("bookId") Long bookId, @AuthenticationPrincipal ShopMemberDetail shopMemberDetail) {
        Long customerId = shopMemberDetail.getCustomerId();
        likeService.addLikeInBook(customerId, bookId);
        return ResponseEntity.ok().body(new MessageDto("좋아요 등록 성공!"));
    }

    //좋아요 삭제
    @DeleteMapping("/like/{bookId}")
    public ResponseEntity<MessageDto> deleteLike(@PathVariable("bookId") Long bookId, @AuthenticationPrincipal ShopMemberDetail shopMemberDetail){
        Long customerId = shopMemberDetail.getCustomerId();
        likeService.deleteLikeInBook(customerId, bookId);
        return ResponseEntity.ok().body(new MessageDto("좋아요 삭제 성공!"));
    }

    //마이페이지에서 좋아요 누른 도서 확인
    @GetMapping("/like")
    public ResponseEntity<List<BookLikeResponseDto>> getLike(@AuthenticationPrincipal ShopMemberDetail shopMemberDetail) {
        Long id = shopMemberDetail.getCustomerId();
        List<BookLikeResponseDto> books = likeService.getLikeBookByCustomerId(id);
        return ResponseEntity.ok().body(books);
    }
}
