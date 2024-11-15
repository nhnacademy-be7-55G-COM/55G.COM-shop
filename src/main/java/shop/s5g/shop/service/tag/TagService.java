package shop.s5g.shop.service.tag;

import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;

import java.util.List;

public interface TagService {
    void createtag(TagRequestDto tagDto);
    List<TagResponseDto> allTag();
    void updateTag(Long tagId, TagRequestDto tagDto);
    void deleteTags(Long tagId);
}
