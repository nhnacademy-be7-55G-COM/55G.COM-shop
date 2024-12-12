package shop.s5g.shop.repository.author;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorResponseDto;
import shop.s5g.shop.entity.Author;
import shop.s5g.shop.entity.QAuthor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    private TestEntityManager testEntityManager;


    /**
     * 작가 전체 조회 test
     */
    @Test
    void findAllAuthorTest() {
        // given
        Author author1 = new Author("Author1", true);
        Author author2 = new Author("Author2", true);
        Author author3 = new Author("Author3", false); // 비활성화 작가

        authorRepository.save(author1);
        authorRepository.save(author2);
        authorRepository.save(author3);

        Pageable pageable = PageRequest.of(0, 2);

        // when
        Page<AllAuthorResponseDto> result = authorRepository.findAllAuthor(pageable);

        // then
        assertEquals(2, result.getContent().size());
        assertEquals("Author1", result.getContent().get(0).name());
        assertEquals("Author2", result.getContent().get(1).name());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
    }

    /**
     * 작가 전체조회 페이징 테스트
     */
    @Test
    void findAllAuthorPagingTest() {
        // Given: 테스트 데이터를 생성 및 저장
        for (int i = 1; i <= 10; i++) {
            authorRepository.save(new Author("Author" + i, true));
        }

        Pageable pageable = PageRequest.of(1, 5); // 두 번째 페이지, 페이지 크기 5

        // When: findAllAuthor 호출
        Page<AllAuthorResponseDto> result = authorRepository.findAllAuthor(pageable);

        // Then: 결과 검증
        assertEquals(5, result.getContent().size());
        assertEquals("Author6", result.getContent().get(0).name());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
    }

    /**
     * 작가 id로 작가 조회
     */
    @Test
    void getAuthorByIdSuccessTest() {
        // Given: 테스트 데이터 생성 및 저장
        Author savedAuthor = authorRepository.save(new Author("한강", true));

        // When: 작가 ID로 조회
        AuthorResponseDto result = jpaQueryFactory.select(Projections.constructor(AuthorResponseDto.class,
                        QAuthor.author.authorId,
                        QAuthor.author.name,
                        QAuthor.author.active
                ))
                .from(QAuthor.author)
                .where(QAuthor.author.authorId.eq(savedAuthor.getAuthorId()))
                .fetchOne();

        // Then: 결과 검증
        assertEquals(savedAuthor.getAuthorId(), result.authorId());
        assertEquals(savedAuthor.getName(), result.name());
        assertTrue(result.active());
    }

    /**
     * 작가 수정 test
     */
    @Test
    void updateAuthorSuccessTest() {
        // Given: 테스트 데이터 생성 및 저장
        Author savedAuthor = authorRepository.save(new Author("한강", true));

        String updatedName = "낙동강";

        // When: 작가 이름 수정
        long rowsAffected = jpaQueryFactory.update(QAuthor.author)
                .set(QAuthor.author.name, updatedName)
                .where(QAuthor.author.authorId.eq(savedAuthor.getAuthorId()))
                .execute();

        testEntityManager.flush();
        testEntityManager.clear();

        // Then
        assertEquals(1, rowsAffected);

        Author updatedAuthor = authorRepository.findById(savedAuthor.getAuthorId()).orElseThrow();
        assertEquals(updatedName, updatedAuthor.getName());
        assertTrue(updatedAuthor.isActive());
    }

    /**
     * 작가 삭제(비활성화) test
     */
    @Test
    void inactiveAuthorSuccessTest() {
        // Given: 테스트 데이터 생성 및 저장
        Author savedAuthor = authorRepository.save(new Author("활성화된 작가", true));

        // When: 작가 비활성화
        long rowsAffected = jpaQueryFactory.update(QAuthor.author)
                .set(QAuthor.author.active, false)
                .where(QAuthor.author.authorId.eq(savedAuthor.getAuthorId()))
                .execute();

        // 영속성 컨텍스트 초기화
        testEntityManager.flush();
        testEntityManager.clear();

        // Then
        assertEquals(1, rowsAffected);

        Author updatedAuthor = authorRepository.findById(savedAuthor.getAuthorId()).orElseThrow();
        assertFalse(updatedAuthor.isActive());
    }

    /**
     * 이름으로 작가 존재 확인 test
     */
    @Test
    void existsByNameSuccessTest() {
        // Given
        String authorName = "테스트 작가";
        authorRepository.save(new Author(authorName, true));

        // When
        boolean exists = jpaQueryFactory.selectOne()
                .from(QAuthor.author)
                .where(QAuthor.author.name.eq(authorName))
                .fetchFirst() != null;

        // Then
        assertTrue(exists); // 작가가 존재해야 함
    }

    /**
     * 작가 이름 검색 test
     */
    @Test
    void findByAuthorNameListSuccessTest() {
        // Given: 테스트 데이터 생성
        authorRepository.save(new Author("홍길동", true));
        authorRepository.save(new Author("김길동", true));
        authorRepository.save(new Author("박길동", false));

        String keyword = "길동";

        // When: 키워드로 검색
        List<AuthorResponseDto> result = jpaQueryFactory
                .select(Projections.constructor(AuthorResponseDto.class,
                        QAuthor.author.authorId,
                        QAuthor.author.name,
                        QAuthor.author.active))
                .from(QAuthor.author)
                .where(QAuthor.author.name.contains(keyword))
                .where(QAuthor.author.active.eq(true))
                .fetch();

        // Then: 결과 검증
        assertNotNull(result);
        assertEquals(2, result.size()); // 활성화된 작가만 2명 반환
        assertTrue(result.stream().anyMatch(author -> author.name().equals("홍길동")));
        assertTrue(result.stream().anyMatch(author -> author.name().equals("김길동")));
        assertFalse(result.stream().anyMatch(author -> author.name().equals("박길동"))); // 비활성화된 작가는 반환되지 않음
    }
}