package shop.s5g.shop.service.tag.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;
import shop.s5g.shop.entity.Tag;
import shop.s5g.shop.exception.booktag.BookTagAlreadyExistsException;
import shop.s5g.shop.exception.tag.TagAlreadyExistsException;
import shop.s5g.shop.exception.tag.TagResourceNotFoundException;
import shop.s5g.shop.repository.booktag.BookTagRepository;
import shop.s5g.shop.repository.tag.TagRepository;
import shop.s5g.shop.service.tag.TagService;

@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final BookTagRepository bookTagRepository;

    //태그 등록
    public void createtag(TagRequestDto tagDto) {

        if (tagRepository.existsByTagName(tagDto.tagName())) {
            throw new TagAlreadyExistsException(tagDto.tagName() + " 가 이미 존재합니다.");
        }

        Tag tag = new Tag(tagDto.tagName());
        tagRepository.save(tag);
    }

    //모든 태그 리스트 조회
    @Override
    public Page<TagResponseDto> allTag(Pageable pageable) {
        return tagRepository.findAllTag(pageable);
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
//        if(!bookTagRepository.BookTagCount(tagId).isEmpty()) {
//            throw new BookTagAlreadyExistsException("이 태그는 도서에 적용되어 있습니다.");
//        }
        tagRepository.inactiveTag(tagId);
    }
}
