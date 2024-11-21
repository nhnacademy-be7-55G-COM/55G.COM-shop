package shop.s5g.shop.service.tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;

import java.util.List;

public interface TagService {
    void createtag(TagRequestDto tagDto);
    Page<TagResponseDto> allTag(Pageable pageable);
    void updateTag(Long tagId, TagRequestDto tagDto);
    void deleteTags(Long tagId);
}
