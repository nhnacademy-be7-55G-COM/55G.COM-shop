package shop.s5g.shop.service.category.impl;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.category.CategoryRequestDto;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.dto.category.CategoryUpdateRequestDto;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.exception.category.CategoryResourceNotFoundException;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.service.category.CategoryService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    
    //카테고리 등록
    @Override
    public void createCategory(CategoryRequestDto categoryDto) {

        Category category1 = categoryRepository.findById(categoryDto.parentCategoryId())
                .orElseThrow(() -> new CategoryResourceNotFoundException("카테고리가 존재하지 않습니다."));

        Category category = new Category(category1, categoryDto.categoryName(), true);
        categoryRepository.save(category);
    }

    //모든 카테고리 조회
    @Override
    public Page<CategoryResponseDto> allCategory(Pageable pageable) {
        return categoryRepository.getAllCategory(pageable);
    }

    //자식 카테고리 조회
    @Override
    public List<CategoryResponseDto> getChildCategory(long categoryId) {
        return categoryRepository.getChild_Category(categoryId);
    }

    //국내도서 하위 카테고리
    @Override
    public List<CategoryResponseDto> getKoreaBooks() {
        return categoryRepository.getKoreaBook();
    }

    // 아이디로 조회
    @Override
    public CategoryResponseDto getCategory(Long categoryId) {

        if (Objects.isNull(categoryId) || categoryId <= 0) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryResourceNotFoundException("해당 카테고리는 존재하지 않습니다."));

        Long parentCategoryId = (category.getParentCategory() != null) ? category.getParentCategory().getCategoryId() : null;

        return new CategoryResponseDto(
            category.getCategoryId(),
            parentCategoryId,
            category.getCategoryName(),
            category.isActive()
        );
    }

    //카테고리 수정
    @Override
    public void updateCategory(Long categoryId, CategoryUpdateRequestDto categoryDto) {
        if(!categoryRepository.findById(categoryId).isPresent()) {
            throw new CategoryResourceNotFoundException(categoryId + " 는 존재하지 않습니다.");
        }

        categoryRepository.updatesCategory(categoryId, categoryDto);
    }


    //카테고리 삭제(비활성화)
    @Override
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryResourceNotFoundException("Category with id " + categoryId + " not found");
        }
        categoryRepository.inactiveCategory(categoryId);
    }

}