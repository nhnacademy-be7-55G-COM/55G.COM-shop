package shop.S5G.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.BookDto;
import shop.S5G.shop.dto.TagDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.service.TagService;

@RestController
@RequestMapping("/api")
public class TagController {
    private final TagService tagService;
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
    //태그 추가
    @PostMapping("/tag")
    public ResponseEntity addBook(@Validated @RequestBody TagDto tagdto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BadRequestException("잘못된 입력입니다.");
        }
        Tag tag = new Tag(tagdto.getPublisherId(), tagdto.getPublisherName(), tagdto.isActive());
        tagService.createtag(tag);
        return ResponseEntity.ok().body("success");
    }
}
