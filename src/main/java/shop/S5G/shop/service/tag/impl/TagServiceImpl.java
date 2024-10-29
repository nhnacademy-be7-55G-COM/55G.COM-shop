package shop.S5G.shop.service.tag.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.tag.TagRequestDto;
import shop.S5G.shop.dto.tag.TagResponseDto;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.exception.TagException.TagAlreadyExistsException;
import shop.S5G.shop.exception.TagException.TagResourceNotFoundException;
import shop.S5G.shop.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl {
    @Autowired
    private TagRepository tagRepository;

    //태그 등록
    public void createtag(TagRequestDto tagDto) {

        if (tagRepository.existsByTagNameAndActive(tagDto.getTagName(), tagDto.isActive())) {
            throw new TagAlreadyExistsException(tagDto.getTagName() + " 가 이미 존재합니다.");
        }

        Tag tag = new Tag(tagDto.getTagName(), tagDto.isActive());
        tagRepository.save(tag);
    }

    //모든 태그 리스트 조회
    public List<TagResponseDto> allTag() {
        List<Tag> tags = tagRepository.findAll();
        List<TagResponseDto> tagDtos = new ArrayList<>();
        for(int i=0 ; i<tags.size() ; i++) {
            tags.get(i).setTagName(tags.get(i).getTagName());
            tags.get(i).setActive(tags.get(i).isActive());
        }
        return tagDtos;
    }

    //태그 수정
    public void updateTag(Long tagId, Tag tag) {

        Optional<Tag> tags = tagRepository.findById(tagId);

//        tags.get().setPublisherId(tag.getPublisherId());
        tags.get().setTagName(tag.getTagName());
        tags.get().setActive(tag.isActive());
        tagRepository.save(tags.get());
    }

    //태그 삭제
    public void deleteTags(Long tagId) {
        if(!tagRepository.existsById(tagId)) {
            throw new TagResourceNotFoundException("No tags found with id " + tagId);
        }
        tagRepository.deleteById(tagId);
    }
}
