package shop.S5G.shop.service.tag;

import shop.S5G.shop.dto.tag.TagRequestDto;
import shop.S5G.shop.dto.tag.TagResponseDto;
import shop.S5G.shop.entity.Tag;

import java.util.List;

public interface TagService {
    void createtag(TagRequestDto tagDto);
    List<TagResponseDto> allTag();
    void updateTag(Long tagId, TagRequestDto tagDto);
    void deleteTags(Long tagId);
}
