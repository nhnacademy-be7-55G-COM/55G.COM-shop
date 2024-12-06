package shop.s5g.shop.repository.author;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.Author;
import shop.s5g.shop.repository.author.qdsl.AuthorQuerydslRepository;

public interface AuthorRepository extends JpaRepository<Author, Long>, AuthorQuerydslRepository {

}
