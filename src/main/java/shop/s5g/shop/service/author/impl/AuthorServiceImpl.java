package shop.s5g.shop.service.author.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorRequestDto;
import shop.s5g.shop.dto.author.AuthorResponseDto;
import shop.s5g.shop.entity.Author;
import shop.s5g.shop.exception.author.AuthorAlreadyExistsException;
import shop.s5g.shop.exception.author.AuthorResourceNotFooundException;
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
        //작가 이름 중복 등록 불가능
        if (authorRepository.existsByName(authorRequestDto.name())) {
            throw new AuthorAlreadyExistsException("작가 이름이 존재합니다.");
        }
        Author author = new Author(authorRequestDto.name(), true);
        authorRepository.save(author);
    }

    //작가 전체 조회
    @Override
    public Page<AllAuthorResponseDto> allAuthor(Pageable pageable) {
        return authorRepository.findAllAuthor(pageable);
    }

    //작가 id로 작가 조회
    @Override
    public AuthorResponseDto getAuthor(long authorId) {
        return authorRepository.getAuthor(authorId);
    }

    //작가 수정
    @Override
    public void updateAuthor(long authorId, AuthorRequestDto authorRequestDto) {
        if(!authorRepository.existsById(authorId)) {
            throw new AuthorResourceNotFooundException("수정할 작가가 존재하지 않습니다.");
        }
        authorRepository.updateAuthor(authorId, authorRequestDto);
    }

    //작가 삭제(비활성화)
    @Override
    public void deleteAuthor(long authorId) {
        if(!authorRepository.existsById(authorId)) {
            throw new AuthorResourceNotFooundException("삭제할 작가가 존재하지 않습니다.");
        }
        authorRepository.inactiveAuthor(authorId);
    }

    @Override
    public ResponseEntity<List<AuthorResponseDto>> searchAuthors(String keyword){
        return ResponseEntity.ok().body(authorRepository.findByAuthorNameList(keyword));
    }
}
