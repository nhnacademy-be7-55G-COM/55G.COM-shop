package shop.S5G.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
