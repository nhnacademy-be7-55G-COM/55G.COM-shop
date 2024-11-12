package shop.s5g.shop.repository.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsById(String id);

    Admin findById(String id);
}
