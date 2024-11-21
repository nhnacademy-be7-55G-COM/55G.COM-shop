package shop.s5g.shop.repository.tag.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;

import java.util.List;

public interface TagQuerydslRepository {
    Page<TagResponseDto> findAllTag(Pageable pageable);
    void updateTag(Long tagId, TagRequestDto tagDto);
    void inactiveTag(Long tagId);
}
