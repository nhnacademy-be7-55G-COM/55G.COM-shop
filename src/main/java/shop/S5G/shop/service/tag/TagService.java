package shop.S5G.shop.service.tag;

import shop.S5G.shop.dto.tag.TagRequestDto;
import shop.S5G.shop.dto.tag.TagResponseDto;
import shop.S5G.shop.entity.Tag;

import java.util.List;

public interface TagService {
    public void createtag(TagRequestDto tagDto);
    public List<TagResponseDto> allTag();
    public void updateTag(Long tagId, Tag tag);
    public void deleteTags(Long tagId);
}
