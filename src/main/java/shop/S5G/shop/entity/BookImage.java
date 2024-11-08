package shop.S5G.shop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_image_id")
    private Long bookImageId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "image_name")
    private String imageName;
}