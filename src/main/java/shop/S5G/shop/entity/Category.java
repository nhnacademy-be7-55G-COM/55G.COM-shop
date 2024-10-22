package shop.S5G.shop.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_id;

    @ManyToOne
    @JoinColumn(name = "category_super_id")
    private Category parent_category;

    @OneToMany(mappedBy = "parent_category")
    private List<Category> children_category;

    private String category_name;
    private boolean active;
}