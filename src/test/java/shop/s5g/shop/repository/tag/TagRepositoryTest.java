package shop.s5g.shop.repository.tag;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;
import shop.s5g.shop.entity.Tag;
import shop.s5g.shop.exception.tag.TagResourceNotFoundException;
import shop.s5g.shop.repository.tag.qdsl.impl.TagQuerydslRepositoryImpl;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagQuerydslRepositoryImpl tagQuerydslRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    /**
     * 테그 등록 test
     */
    @Test
    @DisplayName("테그 등록 test")
    void addTagTest() {
        Tag tag = new Tag("베스트셀러", true);
        Tag save = tagRepository.save(tag);
        Assertions.assertEquals("베스트셀러", save.getTagName());
    }

    /**
     * 테그 조회 test
     */
    @Test
    @DisplayName("테그 조회 test")
    void getTagTest() {
        Tag tag1 = new Tag("베스트셀러", true);
        Tag tag2 = new Tag("이달의 도서", true);
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        List<TagResponseDto> allTag = tagQuerydslRepository.findAllTag();
        Assertions.assertEquals(2, allTag.size());
    }

    /**
     * 테그 수정 test
     */
    @Test
    @DisplayName("테그 수정 test")
    void updateTagTest() {
        Tag oldTag = new Tag("5월 베스트셀러", true);
        TagRequestDto newTag = new TagRequestDto("6월 베스트셀러", true);

        //oldTag 저장
        Tag save = tagRepository.save(oldTag);
        //newTag로 수정
        tagRepository.updateTag(save.getTagId(), newTag);

        //영속성 컨텍스트를 갱신해 변경사항을 반영
        testEntityManager.flush();
        testEntityManager.clear();

        //DB에서 다시 조회하여 반영 확인
        Tag tag = tagRepository.findById(save.getTagId())
                .orElseThrow(() -> new TagResourceNotFoundException("해당 테그는 존재하지 않습니다."));
        Assertions.assertEquals("6월 베스트셀러", tag.getTagName());
    }

    /**
     * 테그 삭제 test
     */
    @Test
    @DisplayName("테그 삭제(비활성화) test")
    void deleteTagTest() {
        Tag oldTag = new Tag("5월 베스트셀러", true);
        Tag save = tagRepository.save(oldTag);

        tagRepository.inactiveTag(save.getTagId());

        testEntityManager.flush();
        testEntityManager.clear();

        Tag tag = tagRepository.findById(save.getTagId())
                .orElseThrow(() -> new TagResourceNotFoundException("해당 테그는 존재하지 않습니다."));

        Assertions.assertEquals(false, tag.isActive());
    }
}
