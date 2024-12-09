package shop.s5g.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_super_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> childCategory;

    @Column(name = "category_name")
    private String categoryName;

    @Setter
    private boolean active;

    public Category(Category parentCategory, String categoryName, boolean active) {
        this.parentCategory = parentCategory;
        this.categoryName = categoryName;
        this.active = active;
    }
}