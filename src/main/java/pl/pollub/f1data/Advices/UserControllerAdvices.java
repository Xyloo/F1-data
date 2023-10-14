package pl.pollub.f1data.Advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.pollub.f1data.Exceptions.EmailExistsException;
import pl.pollub.f1data.Exceptions.InvalidPasswordException;
import pl.pollub.f1data.Exceptions.UserNotFoundException;
import pl.pollub.f1data.Exceptions.UsernameExistsException;

@ControllerAdvice
public class UserControllerAdvices {

    @ExceptionHandler(EmailExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleEmailExistsException(EmailExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidPasswordException(InvalidPasswordException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UsernameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleUsernameExistsException(UsernameExistsException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException ex) {
        return ex.getMessage();
    }
}
