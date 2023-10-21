package pl.pollub.f1data.Advices;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.pollub.f1data.Exceptions.EmailExistsException;
import pl.pollub.f1data.Exceptions.InvalidPasswordException;
import pl.pollub.f1data.Exceptions.UserNotFoundException;
import pl.pollub.f1data.Exceptions.UsernameExistsException;

import java.nio.file.AccessDeniedException;

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

    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable cause = ex.getCause().getCause();
        if (cause instanceof ConstraintViolationException) {
            StringBuilder message = new StringBuilder();
            for (ConstraintViolation<?> violation : ((ConstraintViolationException) cause).getConstraintViolations()) {
                message.append(violation.getMessage()).append('\n');
            }
            return ResponseEntity.badRequest().body("Error: " + message);
        }
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest().body("Error: Duplicate entry. Username or email already exists.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Access denied.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Username and/or password are incorrect.");
    }

}
