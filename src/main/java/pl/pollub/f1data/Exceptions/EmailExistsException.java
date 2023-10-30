package pl.pollub.f1data.Exceptions;

/**
 * Exception thrown when email already exists in database
 */
public class EmailExistsException extends RuntimeException{
    /**
     * Constructor
     */
    public EmailExistsException() {
        super("Email already exists");
    }
}
