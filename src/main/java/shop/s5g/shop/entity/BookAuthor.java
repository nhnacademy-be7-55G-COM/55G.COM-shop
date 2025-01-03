package shop.s5g.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookAuthorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorTypeId")
    private AuthorType authorType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorId")
    private Author author;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookAuthor(Book book,Author author,AuthorType authorType,LocalDateTime createdAt,LocalDateTime updatedAt){
        this.book=book;
        this.author=author;
        this.authorType=authorType;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }
}