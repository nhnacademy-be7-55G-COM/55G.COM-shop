package shop.s5g.shop.repository.category.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.dto.category.CategoryUpdateRequestDto;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.repository.category.qdsl.CategoryQuerydslRepository;

import java.util.List;

import static shop.s5g.shop.entity.QCategory.category;

@Repository
public class CategoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CategoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CategoryQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Category.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //카테고리 수정
    @Override
    public void updatesCategory(Long categoryId, CategoryUpdateRequestDto categoryDto) {
        jpaQueryFactory .update(category)
                .set(category.categoryName, categoryDto.categoryName())
                .set(category.active, categoryDto.active())
                .where(category.categoryId.eq(categoryId))
                .execute();//쿼리를 최종적으로 실행하여 변경 내용을 db에 반영
    }

    //모든 카테고리 조회
    @Override
    public List<CategoryResponseDto> getAllCategory() {
        return jpaQueryFactory
                .from(category)
                .select(Projections.constructor(CategoryResponseDto.class,
                        category.categoryId,
                        category.parentCategory.categoryId, //부모카테고리 이름 리턴
                        category.categoryName,
                        category.active))
                .where(category.active.eq(true))
                .fetch();
    }

    //자식 카테고리 조회
    @Override
    public List<CategoryResponseDto> getChild_Category(Long categoryId) {
        return jpaQueryFactory
                .from(category)
                .select(Projections.constructor(CategoryResponseDto.class,
                        category.categoryId,
                        category.parentCategory.categoryId,
                        category.categoryName,
                        category.active))
                .where(category.parentCategory.categoryId.eq(categoryId))
                .fetch();
    }

    //국내도서 하위 카테고리 조회
    @Override
    public List<CategoryResponseDto> getKoreaBook() {
        return jpaQueryFactory
                .from(category)
                .select(Projections.constructor(CategoryResponseDto.class,
                        category.categoryId,
                        category.parentCategory.categoryId,
                        category.categoryName,
                        category.active))
                .where(category.parentCategory.isNull())
                .fetch();
    }

    //카테고리 비활성화
    @Override
    public void inactiveCategory(Long categoryId) {
        update(category)
                .set(category.active, false)
                .where(category.categoryId.eq(categoryId))
                .execute();
    }
}
