package shop.s5g.shop.service.author;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorRequestDto;

public interface AuthorService {
    void createAuthor(@Valid AuthorRequestDto authorRequestDto);

    Page<AllAuthorResponseDto> allAuthor(Pageable pageable);
}
