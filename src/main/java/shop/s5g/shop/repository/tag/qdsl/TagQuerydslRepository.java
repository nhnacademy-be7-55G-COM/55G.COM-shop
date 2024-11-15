package shop.s5g.shop.repository.tag.qdsl;

import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;

import java.util.List;

public interface TagQuerydslRepository {
    List<TagResponseDto> findAllTag();
    void updateTag(Long tagId, TagRequestDto tagDto);
    void inactiveTag(Long tagId);
}
