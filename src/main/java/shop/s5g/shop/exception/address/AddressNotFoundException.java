package shop.s5g.shop.exception.address;

import shop.s5g.shop.exception.ResourceNotFoundException;

public class AddressNotFoundException extends ResourceNotFoundException {

    public AddressNotFoundException(String message) {
        super(message);
    }
}
