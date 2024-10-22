package shop.S5G.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.exception.AlreadyExistsException;
import shop.S5G.shop.repository.TagRepository;

import java.util.Optional;

public class TagService {
    @Autowired
    private TagRepository tagRepository;

    //태그 등록
    public void createtag(Tag tag) {
        Optional<Tag> tags = tagRepository.findById(tag.getPublisherId());
        if (tags.isPresent()) {
            throw new AlreadyExistsException("Tag with id " + tag.getPublisherId() + " already exists");
        }
        tagRepository.save(tag);
    }
}
