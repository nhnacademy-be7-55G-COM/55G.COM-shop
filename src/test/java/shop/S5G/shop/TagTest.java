//package shop.S5G.shop;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.DirtiesContext;
//import shop.S5G.shop.entity.Tag;
//import shop.S5G.shop.repository.TagRepository;
//import shop.S5G.shop.service.TagService;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@DataJpaTest
//@ExtendWith(MockitoExtension.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//public class TagTest {
//
//    @Autowired
//    private TagRepository tagRepository;
//    @Autowired
//    private TagService tagService;
//
//
//    /**
//     * 태그 등록
//     */
//    @Test
//    void addTagTest() {
//        Tag tag = new Tag(1L, "인증", true);
//
//        Tag save = tagRepository.save(tag);
//        Optional<Tag> tagOptional = tagRepository.findById(1L);
//        assertEquals(save, tagOptional.get());
//    }
//
//    /**
//     * 태그 목록 조회
//     */
//    @Test
//    void getAllTagsTest() {
//        Tag tag1 = new Tag(1L, "인증", true);
//        Tag tag2 = new Tag(2L, "베스트셀러", false);
//
//        tagRepository.save(tag1);
//        tagRepository.save(tag2);
//        List<Tag> tags = tagService.allTag();
//        assertEquals(tags.size(), 2);
//    }
//
//    /**
//     * 태그 수정
//     */
//    @Test
//    void updateTagTest() {
//        Tag tag1 = new Tag(1L, "베스트셀러", true);
//        Tag tag2 = new Tag(1L, "밀리언셀러", true);
//        Tag tags = tagRepository.save(tag1);
//        tagService.updateTag(tags.getTagId(), tag2);
////        Optional<Tag> tagOptional = tagRepository.findById(1);
//        assertEquals(tags.getTagName(), "밀리언셀러");
//    }
//
//    /**
//     * 태그 삭제
//     */
//    @Test
//    void deleteTagTest() {
//        Tag tag1 = new Tag(1L, "베스트셀러", true);
//        tagRepository.save(tag1);
//        tagService.deleteTags(tag1.getTagId());
//        assertEquals(tagRepository.count(), 0);
//    }
//}
