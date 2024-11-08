package shop.S5G.shop.repository.bookstatus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.S5G.shop.config.QueryFactoryConfig;
import shop.S5G.shop.dto.bookstatus.BookStatusResponseDto;
import shop.S5G.shop.entity.BookStatus;

import java.util.List;

@DataJpaTest
@Import(QueryFactoryConfig.class)
public class BookStatusRepositoryTest {
    @Autowired
    private BookStatusRepository bookStatusRepository;

    /**
     * 도서상태 전체 조회 test
     */
    @Test
    @DisplayName("도서상태 전체 조회 test")
    void getAllBookStatus() {
        BookStatus bookStatus1 = new BookStatus("ONSALE");
        BookStatus bookStatus2 = new BookStatus("OUTOFPOINT");
        BookStatus bookStatus3 = new BookStatus("OUTOFSTOCK");
        BookStatus bookStatus4 = new BookStatus("DROP");

        bookStatusRepository.save(bookStatus1);
        bookStatusRepository.save(bookStatus2);
        bookStatusRepository.save(bookStatus3);
        bookStatusRepository.save(bookStatus4);

        List<BookStatusResponseDto> allBookStatus = bookStatusRepository.findAllBookStatus();
        Assertions.assertEquals(4, allBookStatus.size());

    }
}
