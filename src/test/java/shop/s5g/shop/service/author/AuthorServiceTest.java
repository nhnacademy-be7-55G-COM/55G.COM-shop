package shop.s5g.shop.service.author;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorRequestDto;
import shop.s5g.shop.dto.author.AuthorResponseDto;
import shop.s5g.shop.entity.Author;
import shop.s5g.shop.exception.author.AuthorAlreadyExistsException;
import shop.s5g.shop.exception.author.AuthorResourceNotFooundException;
import shop.s5g.shop.repository.author.AuthorRepository;
import shop.s5g.shop.service.author.impl.AuthorServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;


    /**
     * 작가 등록 성공 test
     */
    @Test
    void createAuthorSuccessTest() {
        // Given
        AuthorRequestDto authorRequestDto = new AuthorRequestDto("한강");
        when(authorRepository.existsByName("한강")).thenReturn(false);

        // When
        authorService.createAuthor(authorRequestDto);

        // Then
        verify(authorRepository, times(1)).existsByName("한강");
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    /**
     * 작가 등록 실패 test
     * 이미 존재하는 작가
     */
    @Test
    void createAuthorDuplicateNameTest() {
        // Given
        AuthorRequestDto authorRequestDto = new AuthorRequestDto("한강");
        when(authorRepository.existsByName("한강")).thenReturn(true);

        // When & Then
        assertThrows(AuthorAlreadyExistsException.class, () -> {
            authorService.createAuthor(authorRequestDto);
        });

        verify(authorRepository, times(1)).existsByName("한강");
        verify(authorRepository, never()).save(any(Author.class));
    }

    /**
     * 작가 전체 조회 test
     */
    @Test
    void allAuthorSuccessTest() {
        // Given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        List<AllAuthorResponseDto> authors = List.of(
                new AllAuthorResponseDto(1L, "한강", true),
                new AllAuthorResponseDto(2L, "낙동강", true)
        );
        Page<AllAuthorResponseDto> mockPage = new PageImpl<>(authors, pageable, authors.size());

        when(authorRepository.findAllAuthor(pageable)).thenReturn(mockPage);

        // When
        Page<AllAuthorResponseDto> result = authorService.allAuthor(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("한강", result.getContent().get(0).name());
        assertEquals("낙동강", result.getContent().get(1).name());

        verify(authorRepository, times(1)).findAllAuthor(pageable);
    }

    /**
     * 작가 id로 작가 조회 test
     */
    @Test
    void getAuthorSuccessTest() {
        // Given
        long authorId = 1L;
        AuthorResponseDto expectedAuthor = new AuthorResponseDto(
                authorId,
                "한강",
                true
        );

        when(authorRepository.getAuthor(authorId)).thenReturn(expectedAuthor);

        // When
        AuthorResponseDto result = authorService.getAuthor(authorId);

        // Then
        assertNotNull(result);
        assertEquals(expectedAuthor.authorId(), result.authorId());
        assertEquals(expectedAuthor.name(), result.name());
        assertEquals(expectedAuthor.active(), result.active());

        verify(authorRepository, times(1)).getAuthor(authorId);
    }

    /**
     * 작가 수정 test
     */
    @Test
    void updateAuthorSuccessTest() {
        // Given
        long authorId = 1L;
        AuthorRequestDto authorRequestDto = new AuthorRequestDto("한강");
        when(authorRepository.existsById(authorId)).thenReturn(true);
        doNothing().when(authorRepository).updateAuthor(authorId, authorRequestDto);

        // When
        authorService.updateAuthor(authorId, authorRequestDto);

        // Then
        verify(authorRepository, times(1)).existsById(authorId);
        verify(authorRepository, times(1)).updateAuthor(authorId, authorRequestDto);
    }

    /**
     * 작가 수정 실패 test
     * 작가가 존재하지 않음
     */
    @Test
    void updateAuthorNotFoundTest() {
        // Given
        long authorId = 999L;
        AuthorRequestDto authorRequestDto = new AuthorRequestDto("한강");
        when(authorRepository.existsById(authorId)).thenReturn(false);

        // When & Then
        assertThrows(AuthorResourceNotFooundException.class, () -> {
            authorService.updateAuthor(authorId, authorRequestDto);
        });

        verify(authorRepository, times(1)).existsById(authorId);
        verify(authorRepository, never()).updateAuthor(anyLong(), any());
    }

    /**
     * 작가 삭제 test
     */
    @Test
    void deleteAuthorSuccessTest() {
        // Given
        long authorId = 1L;
        when(authorRepository.existsById(authorId)).thenReturn(true);
        doNothing().when(authorRepository).inactiveAuthor(authorId);

        // When
        authorService.deleteAuthor(authorId);

        // Then
        verify(authorRepository, times(1)).existsById(authorId);
        verify(authorRepository, times(1)).inactiveAuthor(authorId);
    }

    /**
     * 작가 삭제 실패 test
     * 작가 없음
     */
    @Test
    void deleteAuthorNotFoundTest() {
        // Given
        long authorId = 999L;
        when(authorRepository.existsById(authorId)).thenReturn(false);

        // When & Then
        assertThrows(AuthorResourceNotFooundException.class, () -> {
            authorService.deleteAuthor(authorId);
        });

        verify(authorRepository, times(1)).existsById(authorId);
        verify(authorRepository, never()).inactiveAuthor(anyLong());
    }

}
