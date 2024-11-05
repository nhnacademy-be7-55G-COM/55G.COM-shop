package shop.S5G.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
