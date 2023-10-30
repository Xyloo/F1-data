package pl.pollub.f1data.Exceptions;

/**
 * Exception thrown when user is not found in database.
 */
public class UserNotFoundException extends RuntimeException{
    /**
     * Constructor
     */
    public UserNotFoundException() {
        super("User not found");
    }
}
