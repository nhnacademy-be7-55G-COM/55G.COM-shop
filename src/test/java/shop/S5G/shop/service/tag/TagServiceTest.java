package shop.S5G.shop.service.tag;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.S5G.shop.dto.tag.TagRequestDto;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.exception.tag.TagAlreadyExistsException;
import shop.S5G.shop.exception.tag.TagResourceNotFoundException;
import shop.S5G.shop.repository.tag.TagRepository;
import shop.S5G.shop.service.tag.impl.TagServiceImpl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagServiceImpl;

    /**
     * 태그 등록 test
     */
    @Test
    void addTag() {
        //given
        TagRequestDto tagRequestDto = new TagRequestDto("베스트셀러", true);
        //tagName이 베스트셀러인 tag가 이미 존재한다고 가정하면
        when(tagRepository.existsByTagNameAndActive("베스트셀러", true)).thenReturn(true);

        //when
        assertThatThrownBy(()-> tagServiceImpl.createtag(tagRequestDto)).isInstanceOf(TagAlreadyExistsException.class);

        //then
        verify(tagRepository, never()).save(any(Tag.class));
    }

    /**
     * 태그 삭제 test
     */
    @Test
    void deleteTag() {
        when(tagRepository.existsById(eq(1L))).thenReturn(false);
        assertThatThrownBy(() -> tagServiceImpl.deleteTags(1L)).isInstanceOf(TagResourceNotFoundException.class);
        verify(tagRepository, times(1)).existsById(eq(1L));
        verify(tagRepository, never()).deleteById(eq(1L));
    }
}
