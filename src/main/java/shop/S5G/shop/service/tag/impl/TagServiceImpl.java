package shop.S5G.shop.service.tag.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.tag.TagRequestDto;
import shop.S5G.shop.dto.tag.TagResponseDto;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.exception.tag.TagAlreadyExistsException;
import shop.S5G.shop.exception.tag.TagResourceNotFoundException;
import shop.S5G.shop.repository.tag.TagRepository;
import shop.S5G.shop.service.tag.TagService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    //태그 등록
    public void createtag(TagRequestDto tagDto) {

        if (tagRepository.existsByTagNameAndActive(tagDto.tagName(), tagDto.active())) {
            throw new TagAlreadyExistsException(tagDto.tagName() + " 가 이미 존재합니다.");
        }

        Tag tag = new Tag(tagDto.tagName(), tagDto.active());
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
