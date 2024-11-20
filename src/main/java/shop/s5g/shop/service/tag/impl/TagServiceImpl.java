package shop.s5g.shop.service.tag.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;
import shop.s5g.shop.entity.Tag;
import shop.s5g.shop.exception.tag.TagAlreadyExistsException;
import shop.s5g.shop.exception.tag.TagResourceNotFoundException;
import shop.s5g.shop.repository.tag.TagRepository;
import shop.s5g.shop.service.tag.TagService;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository){
        this.tagRepository=tagRepository;
    }

    //태그 등록
    public void createtag(TagRequestDto tagDto) {

        if (tagRepository.existsByTagName(tagDto.tagName())) {
            throw new TagAlreadyExistsException(tagDto.tagName() + " 가 이미 존재합니다.");
        }

        Tag tag = new Tag(tagDto.tagName());
        tagRepository.save(tag);
    }

    //모든 태그 리스트 조회
    public List<TagResponseDto> allTag() {
        return tagRepository.findAllTag();
    }

    //태그 수정
    public void updateTag(Long tagId, TagRequestDto tagDto) {
        tagRepository.updateTag(tagId, tagDto);
    }

    //태그 삭제
    public void deleteTags(Long tagId) {
        if(!tagRepository.existsById(tagId)) {
            throw new TagResourceNotFoundException(tagId+"태그는 존재하지 않습니다.");
        }
        tagRepository.inactiveTag(tagId);
    }
}
