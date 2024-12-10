package shop.s5g.shop.service.book.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.book.BookDocumentResponseDto;
import shop.s5g.shop.entity.document.BookDocument;
import shop.s5g.shop.repository.book.BookDocumentRepository;

@ExtendWith(MockitoExtension.class)
class BookDocumentServiceImplTest {

    @InjectMocks
    private BookDocumentServiceImpl bookDocumentService;

    @Mock
    private BookDocumentRepository bookDocumentRepository;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    @Test
    void findAllBooks() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDocument> bookDocuments = List.of(new BookDocument());
        Page<BookDocument> bookPage = new PageImpl<>(bookDocuments, pageable, 1);

        when(bookDocumentRepository.findAll(pageable)).thenReturn(bookPage);

        // when
        PageResponseDto<BookDocumentResponseDto> result = bookDocumentService.findAllBooks(
            pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.totalElements());
        verify(bookDocumentRepository, times(1)).findAll(pageable);
    }

    @Test
    void searchByKeyword() {
        // given
        String keyword = "spring";
        Pageable pageable = PageRequest.of(0, 10);

        SearchHits<BookDocument> searchHits = mock(SearchHits.class);

        when(elasticsearchOperations.search(any(CriteriaQuery.class), eq(BookDocument.class)))
            .thenReturn(searchHits);

        when(searchHits.getSearchHits()).thenReturn(List.of());
        when(searchHits.getTotalHits()).thenReturn(0L);

        // when
        PageResponseDto<BookDocumentResponseDto> result = bookDocumentService.searchByKeyword(
            keyword, pageable);

        // then
        assertNotNull(result);
        assertEquals(0, result.totalElements());
        verify(elasticsearchOperations, times(1)).search(any(CriteriaQuery.class),
            eq(BookDocument.class));
    }

    @Test
    void findAllBooksByCategory() {
        // given
        String categoryName = "프로그래밍";
        Pageable pageable = PageRequest.of(0, 10);

        SearchHits<BookDocument> searchHits = mock(SearchHits.class);

        when(elasticsearchOperations.search(any(CriteriaQuery.class), eq(BookDocument.class)))
            .thenReturn(searchHits);

        when(searchHits.getSearchHits()).thenReturn(List.of());
        when(searchHits.getTotalHits()).thenReturn(0L);

        // when
        PageResponseDto<BookDocumentResponseDto> result = bookDocumentService.findAllBooksByCategory(
            categoryName, pageable);

        // then
        assertNotNull(result);
        assertEquals(0, result.totalElements());

    }

    @Test
    void searchByCategoryAndKeyword() {
        // given
        String categoryName = "프로그래밍";
        String keyword = "spring";
        Pageable pageable = PageRequest.of(0, 10);

        SearchHits<BookDocument> searchHits = mock(SearchHits.class);

        when(elasticsearchOperations.search(any(NativeQuery.class), eq(BookDocument.class)))
            .thenReturn(searchHits);

        when(searchHits.getSearchHits()).thenReturn(List.of());
        when(searchHits.getTotalHits()).thenReturn(0L);

        // when
        PageResponseDto<BookDocumentResponseDto> result = bookDocumentService.searchByCategoryAndKeyword(
            categoryName, keyword, pageable);

        // then
        assertNotNull(result);
        assertEquals(0, result.totalElements());
        verify(elasticsearchOperations, times(1)).search(any(NativeQuery.class),
            eq(BookDocument.class));
    }
}