package shop.s5g.shop.service.author.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorRequestDto;
import shop.s5g.shop.entity.Author;
import shop.s5g.shop.repository.author.AuthorRepository;
import shop.s5g.shop.service.author.AuthorService;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    //작가 등록
    @Override
    public void createAuthor(AuthorRequestDto authorRequestDto) {
        Author author = new Author(authorRequestDto.name(), true);
        authorRepository.save(author);
    }

    //작가 전체 조회

    @Override
    public Page<AllAuthorResponseDto> allAuthor(Pageable pageable) {
        return authorRepository.findAllAuthor(pageable);
    }
}
