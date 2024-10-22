package shop.S5G.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.repository.TagRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TagTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    /**
     * 태그 등록
     */
    void addTagTest() {
        Tag tag = new Tag(1, "인증", true);

        Tag save = tagRepository.save(tag);
        Optional<Tag> tagOptional = tagRepository.findById(1);
        assertEquals(save, tagOptional.get());
    }
}
