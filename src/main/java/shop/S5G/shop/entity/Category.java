package shop.S5G.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "category_super_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> childCategory;

    @Column(name = "category_name")
    private String categoryName;
    private boolean active;

    public Category(String categoryName, boolean active) {
        this.categoryName = categoryName;
        this.active = active;
    }

    public Category(Long categoryId, String categoryName, boolean active) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.active = active;
    }

    public Category(Long categoryId, Category parentCategory, String categoryName, boolean active) {
        this.categoryId = categoryId;
        this.parentCategory = parentCategory;
        this.categoryName = categoryName;
        this.active = active;
    }

    public Category(Category parentCategory, String categoryName, boolean active) {
        this.parentCategory = parentCategory;
        this.categoryName = categoryName;
        this.active = active;
    }

    public Category() {
    }
}