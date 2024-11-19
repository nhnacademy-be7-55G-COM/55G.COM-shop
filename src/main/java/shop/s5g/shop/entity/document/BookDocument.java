package shop.s5g.shop.entity.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "55g-book")
@Getter
public class BookDocument {

    @Id
    private Long book_id;
    private Long publisher_id;
    private Long book_status_id;
    private String title;
    private String chapter;
    private String description;
    private LocalDateTime published_date;
    private String isbn;
    private Long price;
    private BigDecimal discount_rate;
    private boolean is_packed;
    private int stock;
    private Long views;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
