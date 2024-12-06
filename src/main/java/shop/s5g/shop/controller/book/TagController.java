package shop.s5g.shop.controller.book;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;
import shop.s5g.shop.exception.tag.TagBadRequestException;
import shop.s5g.shop.service.tag.TagService;
import shop.s5g.shop.service.tag.impl.TagServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    //태그 등록
    @PostMapping("/tag")
    public ResponseEntity<MessageDto> addTag(@Valid @RequestBody TagRequestDto tagDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new TagBadRequestException("잘못된 입력입니다.");
        }
        tagService.createtag(tagDto);
        return ResponseEntity.ok().body(new MessageDto("테그 등록 성공"));
    }

    //태그 목록 조회
    @GetMapping("/tag")
    public ResponseEntity<PageResponseDto<TagResponseDto>> getAllTags(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok().body(PageResponseDto.of(tagService.allTag(pageable)));
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
        tagService.updateTag(tagId, tagDto);
        return ResponseEntity.ok().body(new MessageDto("테그 수정 성공"));
    }

    //태그 삭제
    @DeleteMapping("/tag/{tagId}")
    public ResponseEntity<MessageDto> deleteTag(@Valid @PathVariable("tagId") Long tagId) {
        if(tagId <1L) {
            throw new TagBadRequestException("tagId must be grater than 0.");
        }
        tagService.deleteTags(tagId);
        return ResponseEntity.ok().body(new MessageDto("테그 삭제 성공"));
    }

    // 태그 검색
    @GetMapping("/tags")
    public ResponseEntity<List<TagResponseDto>> searchTags(String keyword){
        return tagService.searchTags(keyword);
    }
}