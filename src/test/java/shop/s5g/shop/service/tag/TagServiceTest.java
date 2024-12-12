package shop.s5g.shop.service.tag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;
import shop.s5g.shop.entity.Tag;
import shop.s5g.shop.entity.booktag.BookTag;
import shop.s5g.shop.exception.booktag.BookTagAlreadyExistsException;
import shop.s5g.shop.exception.tag.TagAlreadyExistsException;
import shop.s5g.shop.exception.tag.TagResourceNotFoundException;
import shop.s5g.shop.repository.booktag.BookTagRepository;
import shop.s5g.shop.repository.tag.TagRepository;
import shop.s5g.shop.service.tag.impl.TagServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @Mock
    private TagRepository tagRepository;

    @Mock
    private BookTagRepository bookTagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    /**
     * 태그 등록 성공 테스트
     */
    @Test
    void addTag_Success() {
        // given
        TagRequestDto tagRequestDto = new TagRequestDto("베스트셀러");

        // tagName이 존재하지 않는 경우
        when(tagRepository.existsByTagName("베스트셀러")).thenReturn(false);

        // when
        tagService.createtag(tagRequestDto);

        // then
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    /**
     * 태그 등록 실패 테스트 (이미 존재하는 태그)
     */
    @Test
    void addTag_Failure_TagAlreadyExists() {
        // given
        TagRequestDto tagRequestDto = new TagRequestDto("베스트셀러");

        // tagName이 이미 존재한다고 가정
        when(tagRepository.existsByTagName("베스트셀러")).thenReturn(true);

        // when
        assertThatThrownBy(() -> tagService.createtag(tagRequestDto))
                .isInstanceOf(TagAlreadyExistsException.class)
                .hasMessageContaining("베스트셀러 가 이미 존재합니다.");

        // then
        verify(tagRepository, never()).save(any(Tag.class));
    }

    /**
     * 모든 태그 리스트 조회 test
     */
    @Test
    void allTag_Success() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("tagName").ascending());

        TagResponseDto tag1 = new TagResponseDto(1L, "베스트셀러", true);
        TagResponseDto tag2 = new TagResponseDto(2L, "추천", false);

        Page<TagResponseDto> mockPage = new PageImpl<>(List.of(tag1, tag2), pageable, 2);

        // Mock repository behavior
        when(tagRepository.findAllTag(pageable)).thenReturn(mockPage);

        // when
        Page<TagResponseDto> result = tagService.allTag(pageable);

        // then
        Assertions.assertEquals("베스트셀러", result.getContent().get(0).tagName());
        Assertions.assertEquals("추천", result.getContent().get(1).tagName());

        verify(tagRepository, times(1)).findAllTag(pageable);
    }

    /**
     * 태그 수정 test
     */
    @Test
    @DisplayName("태그 수정 test")
    void updateTag() {
        //given
        Long tagId = 1L;
        TagRequestDto tag = new TagRequestDto("베스트셀러");

        //태그 존재
        when(tagRepository.existsById(tagId)).thenReturn(true);

        //when
        doNothing().when(tagRepository).updateTag(tagId, tag);
        tagService.updateTag(tagId, tag);

        //then
        verify(tagRepository, times(1)).updateTag(tagId, tag);
    }

    /**
     * 태그 수정 실패 테스트 (태그가 존재하지 않을 경우)
     */
    @Test
    void updateTag_Failure_TagNotFound() {
        // given
        Long tagId = 1L;
        TagRequestDto tagRequestDto = new TagRequestDto("베스트셀러");

        // 태그가 존재하지 않을 때
        when(tagRepository.existsById(tagId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> tagService.updateTag(tagId, tagRequestDto))
                .isInstanceOf(TagResourceNotFoundException.class)
                .hasMessageContaining("수정할 태그가 존재하지 않습니다.");

        verify(tagRepository, times(0)).updateTag(tagId, tagRequestDto);
    }

    /**
     * 태그 삭제 test
     */
    @Test
    void deleteTag() {
        //given
        Long tagId = 1L;

        when(tagRepository.existsById(tagId)).thenReturn(true);
        when(bookTagRepository.BookTagCount(tagId)).thenReturn(List.of());

        //when
        tagService.deleteTags(tagId);

        //then
        verify(tagRepository, times(1)).existsById(tagId);
        verify(bookTagRepository, times(1)).BookTagCount(tagId);
        verify(tagRepository, times(1)).inactiveTag(tagId);
    }

    /**
     * 태그 삭제 실패 test
     * 태그가 존재하지 않는 경우
     */
    @Test
    @DisplayName("삭제할 태그가 존재하지 않는 경우")
    void deleteFailTest() {
        //given
        Long tagId = 1L;

        when(tagRepository.existsById(tagId)).thenReturn(false);

        //when
        assertThatThrownBy(() -> tagService.deleteTags(tagId))
                .isInstanceOf(TagResourceNotFoundException.class)
                .hasMessageContaining("태그는 존재하지 않습니다.");

        //then
        verify(tagRepository, times(1)).existsById(tagId);
    }

    /**
     * 태그 삭제 실패
     * 도서에 태그 달려있음
     */
    @Test
    @DisplayName("태그 삭제 - 실패 (태그가 도서에 연결되어 있음)")
    void deleteTagsInUseTest() {
        // Given
        Long tagId = 1L;

        // Mock 동작 정의
        when(tagRepository.existsById(tagId)).thenReturn(true); // 태그가 존재한다고 설정
        when(bookTagRepository.BookTagCount(tagId)).thenReturn(List.of(new BookTag())); // 태그가 도서에 연결됨

        // When
        assertThrows(
                BookTagAlreadyExistsException.class,
                () -> tagService.deleteTags(tagId)
        );

        //Then
        verify(tagRepository, times(1)).existsById(tagId); // 태그 존재 확인 메서드 호출 검증
        verify(bookTagRepository, times(1)).BookTagCount(tagId); // 도서 연결 확인 메서드 호출 검증
        verify(tagRepository, never()).inactiveTag(tagId); // 태그 비활성화 메서드 호출되지 않아야 함
    }

    /**
     * 태그 검색 test
     */
    @Test
    @DisplayName("태그 검색 test")
    void searchTagsTest() {
        //given
        String keyword = "베스트";
        List<TagResponseDto> mockTags = List.of(
                new TagResponseDto(1L, "베스트셀러", true),
                new TagResponseDto(2L, "베스트상품", true)
        );

        when(tagRepository.findByTagNameList(keyword)).thenReturn(mockTags);

        //when
        ResponseEntity<List<TagResponseDto>> response = tagService.searchTags(keyword);

        //then
        assertEquals("베스트셀러", response.getBody().get(0).tagName(), "베스트셀러");
        verify(tagRepository, times(1)).findByTagNameList(keyword);
    }
}
