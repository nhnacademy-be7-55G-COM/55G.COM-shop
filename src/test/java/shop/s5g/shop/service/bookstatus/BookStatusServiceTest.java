package shop.s5g.shop.service.bookstatus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.book.status.BookStatusResponseDto;
import shop.s5g.shop.repository.book.status.BookStatusRepository;
import shop.s5g.shop.service.bookstatus.impl.BookStatusServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookStatusServiceTest {
    @Mock
    private BookStatusRepository bookStatusRepository;

    @InjectMocks
    private BookStatusServiceImpl bookStatusService;

    /**
     * 도서 상태 전체 조회 test
     */
    @Test
    @DisplayName("도서 상태 전체 조회 성공 테스트")
    void getAllBookStatusSuccessTest() {
        // Given
        List<BookStatusResponseDto> mockBookStatuses = List.of(
                new BookStatusResponseDto("ONSALE"),
                new BookStatusResponseDto("OUTOFPOINT"),
                new BookStatusResponseDto("OUTOFSTOCK")
        );

        when(bookStatusRepository.findAllBookStatus()).thenReturn(mockBookStatuses);

        // When
        List<BookStatusResponseDto> result = bookStatusService.getAllBookStatus();

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("ONSALE", result.get(0).name());
        assertEquals("OUTOFPOINT", result.get(1).name());
        assertEquals("OUTOFSTOCK", result.get(2).name());

        verify(bookStatusRepository, times(1)).findAllBookStatus();
    }
}
