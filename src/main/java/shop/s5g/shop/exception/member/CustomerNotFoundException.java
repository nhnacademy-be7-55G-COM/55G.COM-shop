package shop.s5g.shop.exception.member;


import shop.s5g.shop.exception.ResourceNotFoundException;

public class CustomerNotFoundException extends ResourceNotFoundException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
