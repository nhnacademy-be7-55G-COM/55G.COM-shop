package shop.s5g.shop.repository.author.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorRequestDto;
import shop.s5g.shop.dto.author.AuthorResponseDto;

public interface AuthorQuerydslRepository {
    Page<AllAuthorResponseDto> findAllAuthor(Pageable pageable);

    AuthorResponseDto getAuthor(long authorId);

    void updateAuthor(long authorId, AuthorRequestDto authorRequestDto);

    void inactiveAuthor(long authorId);

    boolean existsByName(String name);
}
