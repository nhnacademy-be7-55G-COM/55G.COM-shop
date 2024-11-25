package shop.s5g.shop.controller.book;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.service.booktag.BookTagService;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class BookTagController {

    private final BookTagService bookTagService;


}
