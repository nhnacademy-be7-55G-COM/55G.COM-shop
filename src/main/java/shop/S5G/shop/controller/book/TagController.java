package shop.S5G.shop.controller.book;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.S5G.shop.dto.tag.TagRequestDto;
import shop.S5G.shop.dto.tag.TagResponseDto;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.exception.TagException.TagBadRequestException;
import shop.S5G.shop.service.tag.impl.TagServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class TagController {
    private final TagServiceImpl tagServiceImpl;
    public TagController(TagServiceImpl tagServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
    }
    //태그 등록
    @PostMapping("/tag")
    public ResponseEntity addBook(@Valid @RequestBody TagRequestDto tagDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new TagBadRequestException("잘못된 입력입니다.");
        }
        tagServiceImpl.createtag(tagDto);
        return ResponseEntity.ok().body("success");
    }

    //태그 목록 조회
    @GetMapping("/tag")
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        List<TagResponseDto> tagResponseDtos = tagServiceImpl.allTag();
        return ResponseEntity.ok().body(tagResponseDtos);
    }

    //태그 수정
    @PutMapping("/tag/{tagId}")
    public ResponseEntity updateTag(@Valid @PathVariable("tagId") Long tagId, @Valid @RequestBody TagRequestDto tagdto, BindingResult bindingResult) {
        if(tagId <1L) {
        throw new TagBadRequestException("tagId must be grater than 0.");
        }
        if(bindingResult.hasErrors()) {
            throw new TagBadRequestException("잘못된 입력입니다.");
        }
        Tag tag = new Tag(tagdto.getTagName(), tagdto.isActive());
        tagServiceImpl.updateTag(tagId, tag);
        return ResponseEntity.ok().body("success");
    }

    //태그 삭제
    @DeleteMapping("/tag/{tagId}")
    public ResponseEntity deleteTag(@Valid @PathVariable("tagId") Long tagId) {
        if(tagId <1L) {
            throw new TagBadRequestException("tagId must be grater than 0.");
        }
        tagServiceImpl.deleteTags(tagId);
        return ResponseEntity.ok().body("success");
    }
}