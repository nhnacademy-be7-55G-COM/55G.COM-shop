package shop.s5g.shop.controller.author;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorRequestDto;
import shop.s5g.shop.dto.author.AuthorResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.author.AuthorBadRequestException;
import shop.s5g.shop.service.author.AuthorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class AuthorController {

    private final AuthorService authorService;

    //작가 등록
    @PostMapping("/author")
    public ResponseEntity<MessageDto> addAuthor(@Valid @RequestBody AuthorRequestDto authorRequestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new AuthorBadRequestException("잘못된 입력입니다.");
        }
        authorService.createAuthor(authorRequestDto);
        return ResponseEntity.ok().body(new MessageDto("작가 등록 성공"));
    }

    //모든 작가 조회
    @GetMapping("/author")
    ResponseEntity<PageResponseDto<AllAuthorResponseDto>> getAllAuthors(Pageable pageable){
        Page<AllAuthorResponseDto> authors = authorService.allAuthor(pageable);
        return ResponseEntity.ok().body(PageResponseDto.of(authors));
    }

    //작가 id로 작가 조회
    @GetMapping("/author/{authorId}")
    ResponseEntity<AuthorResponseDto> findAuthor(@PathVariable("authorId") long authorId) {
        AuthorResponseDto authorResponseDto = authorService.getAuthor(authorId);
        return ResponseEntity.ok().body(authorResponseDto);
    }

    //작가 수정
    @PutMapping("/author/{authorId}")
    ResponseEntity<MessageDto> updateAuthor(@Valid @PathVariable("authorId") long authorId, @Valid @RequestBody AuthorRequestDto authorRequestDto, BindingResult bindingResult) {
        if (authorId < 1) {
            throw new AuthorBadRequestException("작가id는 1보다 커야 합니다.");
        }
        if(bindingResult.hasErrors()) {
            throw new AuthorBadRequestException("잘못된 입력입니다.");
        }
        authorService.updateAuthor(authorId, authorRequestDto);
        return ResponseEntity.ok().body(new MessageDto("작가 수정 성공"));
    }

    //작가 삭제(비활성화)
    @DeleteMapping("/author/{authorId}")
    ResponseEntity<MessageDto> deleteAuthor(@PathVariable("authorId") long authorId) {
        if (authorId < 1) {
            throw new AuthorBadRequestException("작가 id는 1보다 커야 합니다.");
        }
        authorService.deleteAuthor(authorId);
        return ResponseEntity.ok().body(new MessageDto("작가 삭제 성공"));
    }

    // 작가 검색
    @GetMapping("/authors")
    ResponseEntity<List<AuthorResponseDto>> searchAuthors(String keyword){
        return authorService.searchAuthors(keyword);
    }
}
