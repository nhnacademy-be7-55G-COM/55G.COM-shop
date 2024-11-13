package shop.s5g.shop.service.member.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.address.AddressRequestDto;
import shop.s5g.shop.dto.address.AddressResponseDto;
import shop.s5g.shop.dto.address.AddressUpdateRequestDto;
import shop.s5g.shop.entity.member.Address;
import shop.s5g.shop.exception.address.AddressNotFoundException;
import shop.s5g.shop.exception.address.TooManyAddressException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.repository.member.AddressRepository;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.service.member.AddressService;

@RequiredArgsConstructor
@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private static final int MAX_SIZE = 10;
    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    @Override
    public void save(AddressRequestDto address, Long customerId) {
        if (addressRepository.countAddressByMember_Id(customerId) == MAX_SIZE) {
            throw new TooManyAddressException("최대 10개까지 등록 가능합니다");
        }
        if (address.isDefault()) {
            changeDefaultAddress(customerId);
        }
        addressRepository.save(new Address(memberRepository.findById(customerId)
            .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다.")),
            address.primaryAddress(),
            address.detailAddress(), address.alias(), address.isDefault()));

    }

    @Override
    public void delete(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new AddressNotFoundException("주소가 존재하지 않습니다.");
        }
        addressRepository.deleteById(addressId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDto> getAddresses(Long customerId) {
        return addressRepository.findByMember_Id(customerId).stream()
            .map(AddressResponseDto::toDto)
            .toList();
    }

    @Override
    public void update(AddressUpdateRequestDto address, Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new AddressNotFoundException("주소가 존재하지 않습니다.");
        }
        Address originAddress = addressRepository.findById(addressId).get();

        if (address.isDefault()) {
            changeDefaultAddress(originAddress.getMember().getId());
            originAddress.setDefault(true);
        } else {
            originAddress.setDefault(false);
        }
        originAddress.setAlias(address.alias());
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponseDto getAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new AddressNotFoundException("주소가 존재하지 않습니다.");
        }
        return AddressResponseDto.toDto(addressRepository.findById(addressId).get());
    }

    @Override
    public void changeDefaultAddress(Long customerId) {
        List<Address> addresses = addressRepository.findByMember_IdAndIsDefault(customerId, true);

        for (Address address : addresses) {
            address.setDefault(false);
        }
    }
}
