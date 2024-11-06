package shop.S5G.shop.controller.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.bookstatus.BookStatusResponseDto;
import shop.S5G.shop.service.bookstatus.BookStatusService;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class BookStatusController {

    private final BookStatusService bookStatusService;
    @Autowired
    public BookStatusController(BookStatusService bookStatusService) {
        this.bookStatusService = bookStatusService;
    }

    //도서 상태 조회
    @GetMapping("/bookstatus")
    public ResponseEntity<List<BookStatusResponseDto>> getAllBookStatus() {
        return ResponseEntity.ok().body(bookStatusService.getAllBookStatus());
    }

}
