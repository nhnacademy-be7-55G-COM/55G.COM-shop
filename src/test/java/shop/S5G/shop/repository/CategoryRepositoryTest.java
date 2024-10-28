package shop.S5G.shop.repository;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.exception.CategoryException.CategoryResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
@DataJpaTest
public class CategoryRepositoryTest {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryRepositoryTest(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 카테고리 등록 test
     */
    @Test
    void addCategoryTest() {
        Category category = new Category();
        Category c = categoryRepository.save(category);
        Optional<Category> id = categoryRepository.findById(c.getCategoryId());
        c.setCategoryName("dd");
        Category saved = categoryRepository.save(c);

        assertEquals(category, id.get());
    }

    /**
     * 카테고리 목록 조회 test
     */
    @Test
    void getAllCategoriesTest() {
        Category category1 = new Category();
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
        Category category1 = new Category();
        Category category2 = new Category(category1, "컴퓨터", true);
        Category category3 = new Category(category1, "인문학", true);
        categoryRepository.save(category1);
        Category save1 = categoryRepository.save(category2);
        Category category = categoryRepository.findById(save1.getCategoryId()).orElseThrow(() -> new CategoryResourceNotFoundException("찾을 수 없습니다."));

        category.setCategoryId(category3.getCategoryId());
        category.setParentCategory(category3.getParentCategory());
        category.setCategoryName(category3.getCategoryName());
        category.setActive(category3.isActive());
        Category saved = categoryRepository.save(category);
        assertEquals(saved.getCategoryName(), "인문학");
    }
    /**
     * 카테고리 삭제 test
     */
    @Test
    void deleteCategoryTest() {
        Category category1 = new Category();
        categoryRepository.save(category1);
        categoryRepository.delete(category1);
        assertEquals(categoryRepository.count(), 0);
    }
}