package pl.pollub.f1data.Exceptions;

/**
 * Exception thrown when username already exists in database.
 */
public class UsernameExistsException extends RuntimeException{
    /**
     * Constructor
     */
    public UsernameExistsException() {
        super("Username already exists");
    }
}
