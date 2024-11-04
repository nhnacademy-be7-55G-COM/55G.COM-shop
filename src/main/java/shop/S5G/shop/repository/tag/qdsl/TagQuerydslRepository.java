package shop.S5G.shop.repository.tag.qdsl;

import shop.S5G.shop.dto.tag.TagRequestDto;
import shop.S5G.shop.dto.tag.TagResponseDto;

import java.util.List;

public interface TagQuerydslRepository {
    List<TagResponseDto> findAllTag();
    void updateTag(Long tagId, TagRequestDto tagDto);
    void inactiveTag(Long tagId);
}
