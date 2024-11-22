package shop.s5g.shop.controller.author;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorRequestDto;
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

}
