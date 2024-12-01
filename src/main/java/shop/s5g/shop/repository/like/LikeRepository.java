package shop.s5g.shop.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.like.Like;
import shop.s5g.shop.entity.like.LikeId;
import shop.s5g.shop.repository.like.qdsl.LikeQuerydslRepository;

public interface LikeRepository extends JpaRepository<Like, LikeId>, LikeQuerydslRepository {
}
