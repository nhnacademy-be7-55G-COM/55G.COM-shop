package shop.S5G.shop.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("Authentication failed");
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
