package shop.s5g.shop.repository.category.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.dto.category.CategoryDetailResponseDto;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.dto.category.CategoryUpdateRequestDto;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.repository.category.qdsl.CategoryQuerydslRepository;

import java.util.List;

import static shop.s5g.shop.entity.QCategory.category;
import static shop.s5g.shop.entity.QPublisher.publisher;

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
        update(category)
                .set(category.categoryName, categoryDto.categoryName())
                .where(category.categoryId.eq(categoryId))
                .execute();//쿼리를 최종적으로 실행하여 변경 내용을 db에 반영
    }

    //모든 카테고리 조회
    @Override
    public Page<CategoryResponseDto> getAllCategory(Pageable pageable) {

        List<CategoryResponseDto> categories = jpaQueryFactory
            .from(category)
            .select(Projections.constructor(CategoryResponseDto.class,
                category.categoryId,
                category.parentCategory.categoryId, //부모카테고리 ID
                category.categoryName,
                category.active))
            .where(category.active.eq(true))
            .offset(pageable.getOffset())  // 페이징의 offset 적용
            .limit(pageable.getPageSize()) // 페이징의 limit 적용
            .fetch();

        Long total = jpaQueryFactory
            .from(category)
            .select(category.count())
            .where(category.active.eq(true))
            .fetchOne();

        total = (total != null) ? total : 0L;

        return new PageImpl<>(categories, pageable, total);
    }

    //자식 카테고리 조회
    @Override
    public List<CategoryResponseDto> getChild_Category(long categoryId) {
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
    public Page<CategoryResponseDto> getKoreaBook(Pageable pageable) {
        List<CategoryResponseDto> categories = jpaQueryFactory
                .select(Projections.constructor(CategoryResponseDto.class,
                        category.categoryId,
                        category.parentCategory.categoryId,
                        category.categoryName,
                        category.active
                ))
                .from(category)
                .where(category.parentCategory.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 쿼리
        Long total = jpaQueryFactory
                .select(category.count())
                .from(category)
                .where(category.parentCategory.isNull())
                .fetchOne();
        total = total != null ? total : 0L; // Null 안전 처리

        // Page 객체 반환
        return new PageImpl<>(categories, pageable, total);
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
