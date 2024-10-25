package shop.S5G.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.exception.TagException.TagAlreadyExistsException;
import shop.S5G.shop.exception.TagException.TagResourceNotFoundException;
import shop.S5G.shop.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    //태그 등록
    public void createtag(Tag tag) {
        Optional<Tag> tags = tagRepository.findById(tag.getTagId());
        if (tags.isPresent()) {
            throw new TagAlreadyExistsException("Tag with id " + tag.getTagId() + " already exists");
        }
        tagRepository.save(tag);
    }

    //모든 태그 리스트 조회
    public List<Tag> allTag() {
        return tagRepository.findAll();
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
