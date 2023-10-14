package pl.pollub.f1data.Exceptions;

public class EmailExistsException extends RuntimeException{
    public EmailExistsException() {
        super("Email already exists");
    }
}
