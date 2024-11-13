package shop.s5g.shop.service.member;

import java.util.List;
import shop.s5g.shop.dto.address.AddressRequestDto;
import shop.s5g.shop.dto.address.AddressResponseDto;
import shop.s5g.shop.dto.address.AddressUpdateRequestDto;

public interface AddressService {

    void save(AddressRequestDto address, Long customerId);

    void delete(Long addressId);

    List<AddressResponseDto> getAddresses(Long customerId);

    void update(AddressUpdateRequestDto address, Long addressId);

    AddressResponseDto getAddress(Long addressId);

    void changeDefaultAddress(Long customerId);
}
