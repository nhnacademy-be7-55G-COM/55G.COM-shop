package shop.s5g.shop.repository.author.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;

public interface AuthorQuerydslRepository {
    Page<AllAuthorResponseDto> findAllAuthor(Pageable pageable);
}
