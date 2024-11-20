package shop.s5g.shop.controller.book;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;
import shop.s5g.shop.exception.tag.TagBadRequestException;
import shop.s5g.shop.service.tag.impl.TagServiceImpl;

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
    public ResponseEntity<MessageDto> addTag(@Valid @RequestBody TagRequestDto tagDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new TagBadRequestException("잘못된 입력입니다.");
        }
        tagServiceImpl.createtag(tagDto);
        return ResponseEntity.ok().body(new MessageDto("테그 등록 성공"));
    }

    //태그 목록 조회
    @GetMapping("/tag")
    public ResponseEntity<List<TagResponseDto>> getAllTags() {
        return ResponseEntity.ok().body(tagServiceImpl.allTag());
    }

    //태그 수정
    @PutMapping("/tag/{tagId}")
    public ResponseEntity<MessageDto> updateTag(@Valid @PathVariable("tagId") Long tagId, @Valid @RequestBody TagRequestDto tagDto, BindingResult bindingResult) {
        if(tagId <1L) {
        throw new TagBadRequestException("tagId must be grater than 0.");
        }
        if(bindingResult.hasErrors()) {
            throw new TagBadRequestException("잘못된 입력입니다.");
        }
        tagServiceImpl.updateTag(tagId, tagDto);
        return ResponseEntity.ok().body(new MessageDto("테그 수정 성공"));
    }

    //태그 삭제
    @DeleteMapping("/tag/{tagId}")
    public ResponseEntity<MessageDto> deleteTag(@Valid @PathVariable("tagId") Long tagId) {
        if(tagId <1L) {
            throw new TagBadRequestException("tagId must be grater than 0.");
        }
        tagServiceImpl.deleteTags(tagId);
        return ResponseEntity.ok().body(new MessageDto("테그 삭제 성공"));
    }
}