package shop.s5g.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
