package shop.s5g.shop.dto.address;

import shop.s5g.shop.entity.member.Address;

public record AddressResponseDto(
    Long addressId,

    String primaryAddress,

    String detailAddress,

    String alias,

    boolean isDefault
) {

    public static AddressResponseDto toDto(Address address) {
        return new AddressResponseDto(address.getAddressId(), address.getPrimaryAddress(),
            address.getDetailAddress(), address.getAlias(), address.isDefault());
    }
}
