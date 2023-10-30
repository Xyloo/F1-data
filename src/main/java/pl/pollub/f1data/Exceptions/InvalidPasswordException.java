package pl.pollub.f1data.Exceptions;

/**
 * Exception thrown when password is invalid
 */
public class InvalidPasswordException extends RuntimeException{
    /**
     * Constructor
     */
    public InvalidPasswordException() {
        super("Invalid password");
    }
}
