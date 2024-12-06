package shop.s5g.shop.service.author;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorRequestDto;
import shop.s5g.shop.dto.author.AuthorResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;

public interface AuthorService {
    void createAuthor(AuthorRequestDto authorRequestDto);

    Page<AllAuthorResponseDto> allAuthor(Pageable pageable);

    AuthorResponseDto getAuthor(long authorId);

    void updateAuthor(long authorId, AuthorRequestDto authorRequestDto);

    void deleteAuthor(long authorId);

    ResponseEntity<List<AuthorResponseDto>> searchAuthors(String keyword);
}
