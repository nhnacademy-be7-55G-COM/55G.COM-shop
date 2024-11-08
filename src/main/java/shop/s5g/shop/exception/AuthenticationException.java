package shop.s5g.shop.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("Authentication failed");
    }

    public AuthenticationException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
