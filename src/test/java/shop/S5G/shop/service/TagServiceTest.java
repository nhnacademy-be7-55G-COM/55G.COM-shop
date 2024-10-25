package shop.S5G.shop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.exception.TagException.TagAlreadyExistsException;
import shop.S5G.shop.exception.TagException.TagResourceNotFoundException;
import shop.S5G.shop.repository.TagRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    /**
     * 태그 등록 test
     */
    @Test
    void addTag() {
        Tag mocktag = new Tag(1L, "인증", true);
        Tag tag = mock(Tag.class);
        //tagId가 1L인 tag가 있다고 가정하면
        when(tagRepository.findById(eq(1L))).thenReturn(Optional.of(tag));
        assertThatThrownBy(() -> tagService.createtag(mocktag)).isInstanceOf(TagAlreadyExistsException.class);

        verify(tagRepository, times(1)).findById(eq(1L));
        verify(tagRepository, never()).save(any(Tag.class));
    }

    /**
     * 태그 삭제 test
     */
    @Test
    void deleteTag() {
        when(tagRepository.existsById(eq(1L))).thenReturn(false);
        assertThatThrownBy(() -> tagService.deleteTags(1L)).isInstanceOf(TagResourceNotFoundException.class);
        verify(tagRepository, times(1)).existsById(eq(1L));
        verify(tagRepository, never()).deleteById(eq(1L));
    }
}
