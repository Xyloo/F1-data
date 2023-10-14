package pl.pollub.f1data.Exceptions;

public class UsernameExistsException extends RuntimeException{
    public UsernameExistsException() {
        super("Username already exists");
    }
}
