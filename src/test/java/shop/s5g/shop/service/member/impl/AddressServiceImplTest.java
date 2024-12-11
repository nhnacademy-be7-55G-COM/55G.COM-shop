package shop.s5g.shop.service.member.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.address.AddressRequestDto;
import shop.s5g.shop.dto.address.AddressResponseDto;
import shop.s5g.shop.dto.address.AddressUpdateRequestDto;
import shop.s5g.shop.entity.member.Address;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.exception.address.AddressNotFoundException;
import shop.s5g.shop.exception.address.TooManyAddressException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.repository.member.AddressRepository;
import shop.s5g.shop.repository.member.MemberRepository;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void save_shouldThrowTooManyAddressException_whenMaxAddressesReached() {
        long customerId = 1L;
        AddressRequestDto addressRequestDto = new AddressRequestDto("123 Main St", "Apt 4B", "Home",
            true);

        when(addressRepository.countAddressByMember_Id(customerId)).thenReturn(10);

        assertThatThrownBy(() -> addressService.save(addressRequestDto, customerId))
            .isInstanceOf(TooManyAddressException.class)
            .hasMessage("최대 10개까지 등록 가능합니다");
    }

    @Test
    void save_shouldThrowMemberNotFoundException_whenMemberDoesNotExist() {
        Long customerId = 1L;
        AddressRequestDto addressRequestDto = new AddressRequestDto("123 Main St", "Apt 4B", "Home",
            true);

        when(memberRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.save(addressRequestDto, customerId))
            .isInstanceOf(MemberNotFoundException.class)
            .hasMessage("존재하지 않는 회원입니다.");
    }

    @Test
    void save_shouldSaveAddressSuccessfully() {
        Long customerId = 1L;
        Member member = new Member();
        AddressRequestDto addressRequestDto = new AddressRequestDto("123 Main St", "Apt 4B", "Home",
            true);

        when(addressRepository.countAddressByMember_Id(customerId)).thenReturn(0);
        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));

        addressService.save(addressRequestDto, customerId);

        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void delete_shouldThrowAddressNotFoundException_whenAddressDoesNotExist() {
        Long addressId = 1L;

        when(addressRepository.existsById(addressId)).thenReturn(false);

        assertThatThrownBy(() -> addressService.delete(addressId))
            .isInstanceOf(AddressNotFoundException.class);
    }

    @Test
    void delete_shouldDeleteAddressSuccessfully() {
        Long addressId = 1L;

        when(addressRepository.existsById(addressId)).thenReturn(true);

        addressService.delete(addressId);

        verify(addressRepository, times(1)).deleteById(addressId);
    }

    @Test
    void getAddresses_shouldReturnAddressList() {
        long customerId = 1L;
        Member member = new Member();
        Address address = new Address(member, "123 Main St", "Apt 4B", "Home", true);
        List<Address> addresses = List.of(address);

        when(addressRepository.findByMember_Id(customerId)).thenReturn(addresses);

        List<AddressResponseDto> responseDtos = addressService.getAddresses(customerId);

        assertThat(responseDtos).hasSize(1);
        assertThat(responseDtos.getFirst().primaryAddress()).isEqualTo("123 Main St");
        assertThat(responseDtos.getFirst().alias()).isEqualTo("Home");
        verify(addressRepository, times(1)).findByMember_Id(customerId);
    }

    @Test
    void update_shouldThrowAddressNotFoundException_whenAddressDoesNotExist() {
        Long addressId = 1L;
        AddressUpdateRequestDto updateRequest = new AddressUpdateRequestDto("Work", false);

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> addressService.update(updateRequest, addressId))
            .isInstanceOf(AddressNotFoundException.class);
    }

    @Test
    void update_shouldUpdateAddressSuccessfully() {
        long addressId = 1L;

        // Mock Member
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L); // 필요에 따라 ID 설정

        // Mock Address
        Address address = new Address(member, "123 Main St", "Apt 4B", "Home", true);
        AddressUpdateRequestDto updateRequest = new AddressUpdateRequestDto("Work", true);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        addressService.update(updateRequest, addressId);

        // Assertions
        assertThat(address.getAlias()).isEqualTo("Work");
        assertThat(address.isDefault()).isTrue();

        // Verify interactions (optional)
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void changeDefaultAddress_shouldSetAllDefaultFlagsToFalse() {
        long customerId = 1L;
        Member member = new Member();
        Address address = new Address(member, "123 Main St", "Apt 4B", "Home", true);
        List<Address> addresses = List.of(address);

        when(addressRepository.findByMember_IdAndIsDefault(customerId, true)).thenReturn(addresses);

        addressService.changeDefaultAddress(customerId);

        assertThat(address.isDefault()).isFalse();
        verify(addressRepository, times(1)).findByMember_IdAndIsDefault(customerId, true);
    }

    @Test
    void getAddress_shouldReturnAddressResponseDto_whenAddressExists() {
        // Given
        long addressId = 1L;
        Member member = mock(Member.class); // Mock Member
        Address address = new Address(addressId, member, "123 Main St", "Apt 4B", "Home", true);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        // When
        AddressResponseDto responseDto = addressService.getAddress(addressId);

        // Then
        assertThat(responseDto.addressId()).isEqualTo(addressId);
        assertThat(responseDto.primaryAddress()).isEqualTo("123 Main St");
        assertThat(responseDto.detailAddress()).isEqualTo("Apt 4B");
        assertThat(responseDto.alias()).isEqualTo("Home");
        assertThat(responseDto.isDefault()).isTrue();

        // Verify
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void getAddress_shouldThrowAddressNotFoundException_whenAddressDoesNotExist() {
        // Given
        long addressId = 1L;

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> addressService.getAddress(addressId))
            .isInstanceOf(AddressNotFoundException.class);

        // Verify
        verify(addressRepository, times(1)).findById(addressId);
    }
}
