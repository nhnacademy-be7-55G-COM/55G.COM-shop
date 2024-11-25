package shop.s5g.shop.exception.address;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class AddressNotFoundException extends ResourceNotFoundException {

    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException() {
        super("주소가 존재하지 않습니다.");
    }
}
