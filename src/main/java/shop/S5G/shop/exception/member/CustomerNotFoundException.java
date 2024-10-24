package shop.S5G.shop.exception.member;


import shop.S5G.shop.exception.ResourceNotFoundException;

public class CustomerNotFoundException extends ResourceNotFoundException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
