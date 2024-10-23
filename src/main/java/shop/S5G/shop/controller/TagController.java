package shop.S5G.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.S5G.shop.dto.BookDto;
import shop.S5G.shop.dto.TagDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.exception.TagException.TagBadRequestException;
import shop.S5G.shop.service.TagService;

import java.util.List;

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
            throw new TagBadRequestException("잘못된 입력입니다.");
        }
        Tag tag = new Tag(tagdto.getPublisherName(), tagdto.isActive());
        tagService.createtag(tag);
        return ResponseEntity.ok().body("success");
    }

    //태그 목록 조회
    @GetMapping("/tag")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.allTag();
        return ResponseEntity.ok().body(tags);
    }

    //태그 수정
    @PutMapping("/tag/{tagId}")
    public ResponseEntity updateTag(@Validated @PathVariable("tagId") Long tagId, @Validated @RequestBody TagDto tagdto, BindingResult bindingResult) {
        if(tagId <1L) {
        throw new TagBadRequestException("tagId must be grater than 0.");
        }
        if(bindingResult.hasErrors()) {
            throw new TagBadRequestException("잘못된 입력입니다.");
        }
        Tag tag = new Tag(tagdto.getPublisherName(), tagdto.isActive());
        tagService.updateTag(tagId, tag);
        return ResponseEntity.ok().body("success");
    }

    //태그 삭제
    @DeleteMapping("/tag/{tagId}")
    public ResponseEntity deleteTag(@Validated @PathVariable("tagId") Long tagId) {
        if(tagId <1L) {
            throw new TagBadRequestException("tagId must be grater than 0.");
        }
        tagService.deleteTags(tagId);
        return ResponseEntity.ok().body("success");
    }
}
