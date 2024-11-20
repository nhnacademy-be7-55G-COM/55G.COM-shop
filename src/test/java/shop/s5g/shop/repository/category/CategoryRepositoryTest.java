package shop.s5g.shop.repository.category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import shop.s5g.shop.config.QueryFactoryConfig;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.category.CategoryUpdateRequestDto;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.exception.category.CategoryResourceNotFoundException;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
class CategoryRepositoryTest {

    private final CategoryRepository categoryRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    public CategoryRepositoryTest(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 카테고리 등록 test
     */
    @Test
    @DisplayName("카테고리 등록 test")
    void addCategoryTest() {
        Category category = new Category(null, "컴퓨터", true);
        Category c = categoryRepository.save(category);

        assertEquals(1, categoryRepository.findAll().size());
    }

    /**
     * 카테고리 목록 조회 test
     */
    @Test
    void getAllCategoriesTest() {
        Category category1 = new Category(null, "헬스", true);
        Category category2 = new Category(category1, "컴퓨터", true);

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        List<Category> categories = categoryRepository.findAll();

        assertEquals(2, categories.size());
    }

    /**
     * 카테고리 수정 test
     */
    @Test
    void updateCategoryTest() {
        Category category1 = new Category(null, "음악", true);
        Category category2 = new Category(category1, "컴퓨터", true);

        Category saved1 = categoryRepository.save(category1);
        Category saved2 = categoryRepository.save(category2);

        CategoryUpdateRequestDto category3 = new CategoryUpdateRequestDto("인문학");

        //category2를 category3으로 수정
        categoryRepository.updatesCategory(saved2.getCategoryId(), category3);

        //영속성 컨텍스트를 갱신해 변경사항을 반영
        testEntityManager.flush();
        testEntityManager.clear();

        //DB에서 다시 조회하여 반영 확인
        Category newCategory = categoryRepository.findById(saved2.getCategoryId())
                .orElseThrow(() -> new CategoryResourceNotFoundException("해당 카테고리는 존재하지 않습니다."));
        Assertions.assertEquals("인문학", newCategory.getCategoryName());

    }
    /**
     * 카테고리 삭제 test
     */
    @Test
    void deleteCategoryTest() {
        Category category = new Category(null, "음악", true);
        Category saved = categoryRepository.save(category);

        categoryRepository.inactiveCategory(saved.getCategoryId());

        testEntityManager.flush();
        testEntityManager.clear();

        Category result = categoryRepository.findById(saved.getCategoryId())
                .orElseThrow(() -> new CategoryResourceNotFoundException("해당 카테고리는 존재하지 않습니다."));
        Assertions.assertEquals(false, result.isActive());

    }
}