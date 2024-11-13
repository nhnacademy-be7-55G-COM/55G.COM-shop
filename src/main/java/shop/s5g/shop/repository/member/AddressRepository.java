package shop.s5g.shop.repository.member;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.member.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

    int countAddressByMember_Id(long customerId);

    List<Address> findByMember_Id(long customerId);

    List<Address> findByMember_IdAndIsDefault(long customerId, boolean defaultValue);
}
