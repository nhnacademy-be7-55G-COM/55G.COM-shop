package shop.S5G.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


public class BookCategory {
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
